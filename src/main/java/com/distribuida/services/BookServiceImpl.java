package com.distribuida.services;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookServiceImpl implements BookService {

    @ConfigProperty(name = "cassandra.table")
    @Inject
    private String table;

    @Inject
    private CqlSession cqlSession;

    @Inject
    @ConfigProperty(name = "cassandra.keyspace")
    private String keyspace;

    @Override
    public void createBook(Book book) {
        book.setId(UUID.randomUUID());
        cqlSession.execute("USE " + CqlIdentifier.fromCql(keyspace));
        var insertStatement = QueryBuilder.insertInto(table)
                .value("id", QueryBuilder.bindMarker())
                .value("isbn", QueryBuilder.bindMarker())
                .value("title", QueryBuilder.bindMarker())
                .value("author", QueryBuilder.bindMarker())
                .value("price", QueryBuilder.bindMarker())
                .build();
        var preparedStatement = cqlSession.prepare(insertStatement);
        var statement = preparedStatement.bind()
                .setUuid(0, book.getId())
                .setString(1, book.getIsbn())
                .setString(2, book.getTitle())
                .setString(3, book.getAuthor())
                .setDouble(4, book.getPrice());
        cqlSession.execute(statement);
    }
    @Override
    public List<Book> getBooks() {
        cqlSession.execute("USE " + CqlIdentifier.fromCql(keyspace));
        var query = QueryBuilder.selectFrom(table).all().build();
        return cqlSession.execute(query).all()
                .stream()
                .map(row -> Book.builder()
                        .id(row.getUuid("id"))
                        .isbn(row.getString("isbn"))
                        .title(row.getString("title"))
                        .author(row.getString("author"))
                        .price(row.getDouble("price"))
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public Book getBookById(String id) {
        cqlSession.execute("USE " + CqlIdentifier.fromCql(keyspace));
        UUID uuid = UUID.fromString(id);
        var query = QueryBuilder.selectFrom(table).all()
                .whereColumn("id")
                .isEqualTo(QueryBuilder.literal(uuid))
                .build();
        return cqlSession.execute(query)
                .map(row -> Book.builder()
                        .id(row.getUuid("id"))
                        .isbn(row.getString("isbn"))
                        .title(row.getString("title"))
                        .author(row.getString("author"))
                        .price(row.getDouble("price"))
                        .build())
                .one();
    }

    @Override
    public void updateBook(String id, Book book) {
        cqlSession.execute("USE " + CqlIdentifier.fromCql(keyspace));
        UUID uuid = UUID.fromString(id);
        var query = QueryBuilder.update(table)
                .setColumn("isbn", QueryBuilder.literal(book.getIsbn()))
                .setColumn("title", QueryBuilder.literal(book.getTitle()))
                .setColumn("author", QueryBuilder.literal(book.getAuthor()))
                .setColumn("price", QueryBuilder.literal(book.getPrice()))
                .whereColumn("id")
                .isEqualTo(QueryBuilder.literal(uuid))
                .build();
        cqlSession.execute(query);
    }


    @Override
    public void delete(String id) {
        cqlSession.execute("USE " + CqlIdentifier.fromCql(keyspace));
        UUID uuid = UUID.fromString(id);
        var query = QueryBuilder.deleteFrom(table)
                .whereColumn("id")
                .isEqualTo(QueryBuilder.literal(uuid))
                .build();
        cqlSession.execute(query);
    }

}
