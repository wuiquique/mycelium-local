package com.mycelium.local.dynamic.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mycelium.local.repository.product.Product;
import com.mycelium.local.repository.product.ProductRepo;

import io.micronaut.context.BeanProvider;
import io.micronaut.context.annotation.Any;
import io.micronaut.transaction.SynchronousTransactionManager;
import jakarta.inject.Singleton;

@Singleton
public class SearchManager {

    private final BeanProvider<Connection> connectionProvider;
    private final BeanProvider<SynchronousTransactionManager<Connection>> transactionManagerProvider;
    private final ProductRepo productRepo;

    public SearchManager(
            @Any BeanProvider<Connection> connectionProvider,
            @Any BeanProvider<SynchronousTransactionManager<Connection>> transactionManagerProvider,
            ProductRepo productRepo) {
        this.connectionProvider = connectionProvider;
        this.transactionManagerProvider = transactionManagerProvider;
        this.productRepo = productRepo;
    }

    public List<Product> search(List<SearchCriteria> criteria) {
        Optional<Connection> connection = Optional.empty();
        Optional<SynchronousTransactionManager<Connection>> transactionManager = Optional.empty();
        for (var conn : connectionProvider) {
            connection = Optional.of(conn);
            break;
        }
        for (var tm : transactionManagerProvider) {
            transactionManager = Optional.of(tm);
            break;
        }
        final var conn = connection.get();
        List<Integer> res = transactionManager.get().executeRead(status -> {
            String where = "";
            for (var criterium : criteria) {
                if (criterium instanceof SearchCriteria.TextContains c) {
                    where += " AND (0 = 1";
                    where += " OR UPPER(p.NAME) LIKE CONCAT(CONCAT('%', UPPER(?)), '%')";
                    where += " OR UPPER(p.\"DESC\") LIKE CONCAT(CONCAT('%', UPPER(?)), '%')";
                    where += " OR UPPER(p.BRAND) LIKE CONCAT(CONCAT('%', UPPER(?)), '%')";
                    where += ")";
                } else if (criterium instanceof SearchCriteria.PriceBetween c) {
                    where += " AND (p.PRICE > ? AND p.PRICE < ?)";
                } else if (criterium instanceof SearchCriteria.CategoryIn c) {
                    where += " AND (0 = 1";
                    for (var id : c.ids) {
                        where += " OR p.CATEGORIEID = ?";
                    }
                    where += ")";
                }
            }

            System.out.println("SELECT p.ID FROM PRODUCT p WHERE 1 = 1" + where);

            try (PreparedStatement ps = conn
                    .prepareStatement("SELECT p.ID FROM PRODUCT p WHERE 1 = 1" + where)) {
                int currIndex = 0;
                for (var criterium : criteria) {
                    if (criterium instanceof SearchCriteria.TextContains c) {
                        ps.setString(++currIndex, c.value);
                        ps.setString(++currIndex, c.value);
                        ps.setString(++currIndex, c.value);
                    } else if (criterium instanceof SearchCriteria.PriceBetween c) {
                        ps.setInt(++currIndex, c.min);
                        ps.setInt(++currIndex, c.max);
                    } else if (criterium instanceof SearchCriteria.CategoryIn c) {
                        for (var id : c.ids) {
                            ps.setInt(++currIndex, id);
                        }
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    List<Integer> internalRes = Lists.newArrayList();
                    while (rs.next()) {
                        internalRes.add(rs.getInt("id"));
                    }
                    return internalRes;
                }
            }
        });
        return productRepo.findByIdInList(res);
    }
}