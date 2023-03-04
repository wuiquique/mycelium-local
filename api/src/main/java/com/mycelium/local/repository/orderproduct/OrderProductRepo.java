package com.mycelium.local.repository.orderproduct;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface OrderProductRepo extends GenericRepository<OrderProduct, Integer> {

    @Query("SELECT * FROM \"orderProducts\" WHERE id = :id")
    Optional<OrderProduct> findById(Integer id);

    @Query("SELECT * FROM \"orderProducts\"")
    List<OrderProduct> findAll();

    @Transactional
    @Query("INSERT INTO \"orderProducts\"(\"orderId\", \"productId\", \"quantity\", \"statusId\", \"tracking\", \"time\", \"integOrderId\", \"created\", \"updated\") VALUES(:orderId, :productId, :quantity, :statusId, :tracking, :time, :integOrderId, :created, :updated)")
    void create(int orderId, int productId, int quantity, int statusId, String tracking, int time, int integOrderId,
            Date created, Date updated);
}
