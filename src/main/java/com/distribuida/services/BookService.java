package com.distribuida.services;

import com.distribuida.db.Book;

import java.util.List;

public interface BookService {

    List<Book> getBooks();

    void createBook(Book book);

    Book getBookById(String id);

    void updateBook(String id, Book book);

    void delete(String id);

}
