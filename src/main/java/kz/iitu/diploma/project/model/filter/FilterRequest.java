package kz.iitu.diploma.project.model.filter;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilterRequest implements Serializable {

    private String key;

    private Operator operator;

    private FieldType fieldType;

    private JoinClass joinClass;

    private transient Object value;

    private transient Object valueTo;

    private transient List<Object> values;

    private transient Object valueJoin;

}

