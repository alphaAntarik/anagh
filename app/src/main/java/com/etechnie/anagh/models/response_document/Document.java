package com.etechnie.anagh.models.response_document;

import java.util.ArrayList;
import java.util.List;

public class Document<T> {
    private List<T> records = new ArrayList<T>();
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalRecords;
    public List<T> getRecords() {
        return records;
    }
    public void setRecords(List<T> records) {
        this.records = records;
    }
    public Integer getPageNumber() {
        return pageNumber;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    public Integer getPageSize() {
        return pageSize;
    }
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public Integer getTotalRecords() {
        return totalRecords;
    }
    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }
}
