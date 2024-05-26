package kz.iitu.diploma.project.model.filter;

import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public enum Operator {

    EQUAL {
        public <X, Y> Predicate build(Root<X> root, Class<Y> cls, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            if (cls != null) {
                Join<X, Y> join = root.join(request.getValueJoin().toString(), JoinType.INNER);
                Object value = request.getFieldType().parse(request.getValue().toString());
                Expression<?> key = join.get(request.getKey());
                return cb.and(cb.equal(key, value), predicate);
            } else {
                Object value = request.getFieldType().parse(request.getValue().toString());
                Expression<?> key = root.get(request.getKey());
                return cb.and(cb.equal(key, value), predicate);
            }
        }
    },

    NOT_EQUAL {
        public <X, Y> Predicate build(Root<X> root, Class<Y> cls, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            if (cls != null) {
                Join<X, Y> join = root.join(request.getValueJoin().toString(), JoinType.INNER);
                Object value = request.getFieldType().parse(request.getValue().toString());
                Expression<?> key = join.get(request.getKey());
                return cb.and(cb.notEqual(key, value), predicate);
            } else {
                Object value = request.getFieldType().parse(request.getValue().toString());
                Expression<?> key = root.get(request.getKey());
                return cb.and(cb.notEqual(key, value), predicate);
            }
        }
    },

    LIKE {
        public <X, Y> Predicate build(Root<X> root, Class<Y> cls, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            if (cls != null) {
                Join<X, Y> join = root.join(request.getValueJoin().toString(), JoinType.INNER);
                Object value = request.getValue().toString().toUpperCase();
                Expression<String> key = join.get(request.getKey());
                return cb.and(cb.like(cb.upper(key), "%" + value + "%"), predicate);
            } else {
                Object value = request.getValue().toString().toUpperCase();
                Expression<String> key = root.get(request.getKey());
                return cb.and(cb.like(cb.upper(key), "%" + value + "%"), predicate);
            }
        }
    },

    IN {
        public <X, Y> Predicate build(Root<X> root, Class<Y> cls, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            if (cls != null) {
                Join<X, Y> join = root.join(request.getValueJoin().toString(), JoinType.INNER);
                List<Object> values = request.getValues();
                CriteriaBuilder.In<Object> inClause = cb.in(join.get(request.getKey()));
                for (Object value : values) {
                    inClause.value(request.getFieldType().parse(value.toString()));
                }
                return cb.and(inClause, predicate);
            } else {
                List<Object> values = request.getValues();
                CriteriaBuilder.In<Object> inClause = cb.in(root.get(request.getKey()));
                for (Object value : values) {
                    inClause.value(request.getFieldType().parse(value.toString()));
                }
                return cb.and(inClause, predicate);
            }
        }
    },

    BETWEEN {
        public <X, Y> Predicate build(Root<X> root, Class<Y> cls, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue().toString());
            Object valueTo = request.getFieldType().parse(request.getValueTo().toString());
            if (request.getFieldType() == FieldType.DATE) {
                LocalDateTime startDate = (LocalDateTime) value;
                LocalDateTime endDate = (LocalDateTime) valueTo;
                Expression<LocalDateTime> key = root.get(request.getKey());
                return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate)), predicate);
            }

            if (request.getFieldType() != FieldType.CHAR && request.getFieldType() != FieldType.BOOLEAN) {
                Number start = (Number) value;
                Number end = (Number) valueTo;
                Expression<Number> key = root.get(request.getKey());
                return cb.and(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
            }

            log.info("Can not use between for {} field type.", request.getFieldType());
            return predicate;
        }
    };

    public abstract <X, Y> Predicate build(Root<X> root, Class<Y> cls, CriteriaBuilder cb, FilterRequest request, Predicate predicate);

}