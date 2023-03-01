package com.distribuida.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AppEventos {

    @Inject
    CqlSession cqlSession;

    @Inject
    @ConfigProperty(name = "cassandra.keyspace")
    private String keyspace;

    @Inject
    @ConfigProperty(name = "cassandra.replicas")
    private Integer replicas;

    @Inject
    @ConfigProperty(name = "cassandra.table")
    private String table;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
        createKeyspace();
        useKeyspace();
        createTable();
    }

    public void createKeyspace() {
        var createKeyspace = SchemaBuilder
                .createKeyspace(keyspace)
                .ifNotExists()
                .withSimpleStrategy(replicas)
                .build();
        cqlSession.execute(createKeyspace);
    }

    public void useKeyspace() {
        cqlSession.execute("USE " + CqlIdentifier.fromCql(keyspace));
    }

    public void createTable() {
        var bookTable = SchemaBuilder.createTable(table)
                .ifNotExists()
                .withPartitionKey("id", DataTypes.UUID)
                .withColumn("isbn", DataTypes.TEXT)
                .withColumn("title", DataTypes.TEXT)
                .withColumn("author", DataTypes.TEXT)
                .withColumn("price", DataTypes.DOUBLE)
                .build();
        cqlSession.execute(bookTable);
    }

}
