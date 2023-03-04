package com.mycelium.local.repository.integorderproduct;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import io.micronaut.transaction.annotation.TransactionalAdvice;

@Repository("default")
@JdbcRepository(dialect = Dialect.ORACLE)
public interface IntegOrderProductRepo extends GenericRepository<IntegOrderProduct, Integer> {

    @Query("SELECT * FROM \"integOrderProducts\" WHERE id = :id")
    Optional<IntegOrderProduct> findById(Integer id);

    @Query("SELECT * FROM \"integOrderProducts\"")
    List<IntegOrderProduct> findAll();

    @TransactionalAdvice("default")
    @Transactional
    @Query("INSERT INTO \"integOrderProducts\"(\"orderId\", \"productId\", \"quantity\", \"statusIntegId\", \"statusLocalId\", \"trackingInteg\", \"trackingLocal\", \"timeInteg\", \"timeLocal\", \"integrationId\", \"created\", \"updated\") VALUES(:orderId, :productId, :quantity, :statusIntegId, :statusLocalId, :trackingInteg, :trackingLocal, :timeInteg, :timeLocal, :integrationId, :created, :updated)")
    void create(int orderId, int productId, int quantity, int statusIntegId, int statusLocalId, String trackingInteg,
            String trackingLocal, int timeInteg, int timeLocal, int integrationId,
            Date created, Date updated);
}
