package com.voltforge.app.payload;

import java.util.List;

public class ProductResponse {
    private List<ProductDTO> products;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private Integer totalElements;
    private boolean isLastPage;

    public ProductResponse() {
    }

    public ProductResponse(List<ProductDTO> products,  Integer pageNumber, Integer pageSize, Integer totalPages, Integer totalElements, boolean isLastPage) {
        this.products = products;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isLastPage = isLastPage;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
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

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }
}
