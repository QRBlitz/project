package kz.iitu.diploma.project.model.filter;

import lombok.Data;

import java.io.Serializable;

@Data
public class SortRequest implements Serializable {

    private String key;

    private SortDirection direction;

}
