package com.mycelium.local.dynamic.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;

import io.micronaut.context.BeanProvider;
import io.micronaut.context.annotation.Any;
import io.micronaut.transaction.SynchronousTransactionManager;
import jakarta.inject.Singleton;

@Singleton
public class ReportManager {

    private final BeanProvider<Connection> connectionProvider;
    private final BeanProvider<SynchronousTransactionManager<Connection>> transactionManagerProvider;

    public ReportManager(
            @Any BeanProvider<Connection> connectionProvider,
            @Any BeanProvider<SynchronousTransactionManager<Connection>> transactionManagerProvider) {
        this.connectionProvider = connectionProvider;
        this.transactionManagerProvider = transactionManagerProvider;
    }

    public List<Map<String, ?>> generateReport(Report.Generator generator) {
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

        var query = generator.generateQuery();
        System.out.println(query);
        return transactionManager.get().executeRead(status -> {
            try (PreparedStatement ps = conn
                    .prepareStatement(query)) {

                generator.setParams(ps);

                try (ResultSet rs = ps.executeQuery()) {
                    List<Map<String, ?>> internalRes = Lists.newArrayList();
                    while (rs.next()) {
                        internalRes.add(generator.getRow(rs));
                    }
                    return internalRes;
                }
            }
        });
    }
}