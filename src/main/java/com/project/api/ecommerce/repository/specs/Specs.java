package com.project.api.ecommerce.repository.specs;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

import java.util.Collection;

public class Specs {

    private Specs() {}

    @SuppressWarnings("unchecked")
    private static <T, Y> Path<Y> getPath(Root<T> root, String field) {
        String[] fields = field.split("\\.");
        Path<?> path = root;
        for (String f : fields) {
            path = path.get(f);
        }
        return (Path<Y>) path;
    }

    public static <T> Specification<T> alwaysTrue() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static <T> Specification<T> alwaysFalse() {
        return (root, query, cb) -> cb.disjunction();
    }

    public static <T> Specification<T> equal(String field, Object value) {
        return (root, query, cb) -> cb.equal(getPath(root, field), value);
    }

    public static <T> Specification<T> notEqual(String field, Object value) {
        return (root, query, cb) -> cb.notEqual(getPath(root, field), value);
    }

    public static <T> Specification<T> like(String field, String value) {
        return (root, query, cb) ->
                cb.like(cb.lower(getPath(root, field).as(String.class)), "%" + value.toLowerCase() + "%");
    }

    public static <T> Specification<T> startsWith(String field, String value) {
        return (root, query, cb) ->
                cb.like(cb.lower(getPath(root, field).as(String.class)), value.toLowerCase() + "%");
    }

    public static <T> Specification<T> endsWith(String field, String value) {
        return (root, query, cb) ->
                cb.like(cb.lower(getPath(root, field).as(String.class)), "%" + value.toLowerCase());
    }

    // Corrigido: Removido .as(Comparable.class) e ajustado Generics
    public static <T, V extends Comparable<? super V>> Specification<T> greaterThan(String field, V value) {
        return (root, query, cb) -> cb.greaterThan(getPath(root, field), value);
    }

    public static <T, V extends Comparable<? super V>> Specification<T> greaterThanOrEqual(String field, V value) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(getPath(root, field), value);
    }

    public static <T, V extends Comparable<? super V>> Specification<T> lessThan(String field, V value) {
        return (root, query, cb) -> cb.lessThan(getPath(root, field), value);
    }

    public static <T, V extends Comparable<? super V>> Specification<T> lessThanOrEqual(String field, V value) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(getPath(root, field), value);
    }

    public static <T, V extends Comparable<? super V>> Specification<T> between(String field, V start, V end) {
        return (root, query, cb) -> cb.between(getPath(root, field), start, end);
    }

    public static <T> Specification<T> in(String field, Collection<?> values) {
        return (root, query, cb) -> getPath(root, field).in(values);
    }

    public static <T> Specification<T> notIn(String field, Collection<?> values) {
        return (root, query, cb) -> cb.not(getPath(root, field).in(values));
    }

    public static <T> Specification<T> isNull(String field) {
        return (root, query, cb) -> cb.isNull(getPath(root, field));
    }

    public static <T> Specification<T> isNotNull(String field) {
        return (root, query, cb) -> cb.isNotNull(getPath(root, field));
    }

    public static <T> Specification<T> isTrue(String field) {
        return (root, query, cb) -> cb.isTrue(getPath(root, field).as(Boolean.class));
    }

    public static <T> Specification<T> isFalse(String field) {
        return (root, query, cb) -> cb.isFalse(getPath(root, field).as(Boolean.class));
    }
}