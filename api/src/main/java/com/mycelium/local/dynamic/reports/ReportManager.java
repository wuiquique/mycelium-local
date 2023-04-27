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

/**
 * Generator for reports. Use Micronaut's dependency injection capabilities to get an instance
 */
@Singleton
public class ReportManager {

    /**
     * The Bean provider for the {@link java.sql.Connection} to the database. Note: might return more than one
     * connection, making Micronaut error out. Workaround by iterating over it to get a single instance
     */
    private final BeanProvider<Connection> connectionProvider;
    /**
     * The Bean provider for the {@link io.micronaut.transaction.SynchronousTransactionManager} of the database.
     * Note: might return more than one connection, making Micronaut error out. Workaround by iterating over
     * it to get a single instance
     */
    private final BeanProvider<SynchronousTransactionManager<Connection>> transactionManagerProvider;

    /**
     * The connection and transaction manager are provided by Micronaut through Dependency Injection
     * Not intended to be instantiated by anything other than Micronaut's dependency injection system
     * 
     * @param connectionProvider
     * @param transactionManagerProvider
     */
    public ReportManager(
            @Any BeanProvider<Connection> connectionProvider,
            @Any BeanProvider<SynchronousTransactionManager<Connection>> transactionManagerProvider) {
        this.connectionProvider = connectionProvider;
        this.transactionManagerProvider = transactionManagerProvider;
    }

    /**
     * Build a {@link Report.Generator} definition and pass it to this method to generate a report
     * 
     * @param generator
     * @return A list of maps containing the data of the report
     */
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