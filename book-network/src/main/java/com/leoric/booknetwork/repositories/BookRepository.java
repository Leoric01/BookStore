package com.leoric.booknetwork.repositories;

import com.leoric.booknetwork.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    //Custom query is unnecessary now, I use BookSpecification instead, but this still works and is usable
    @Query("""
            SELECT book FROM Book book
            WHERE book.archived = false
            AND book.shareable = true
            AND book.owner.id != :userId
            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);

    //Custom query is unnecessary now, I use BookSpecification instead, but this still works and is usable
    @Query("""
            SELECT book FROM Book book
            WHERE book.archived = false
            AND book.shareable = true
            AND book.owner.id = :userId
            """)
    Page<Book> findAllBooksOfCurrentUser(Pageable pageable, Integer userId);
}
