package com.leoric.booknetwork.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> withOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Book> withNotOwnerId(Integer ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("owner").get("id"), ownerId);
    }
}
