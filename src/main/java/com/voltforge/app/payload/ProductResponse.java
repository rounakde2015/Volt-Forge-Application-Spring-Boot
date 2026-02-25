package com.voltforge.app.payload;

import java.util.List;

public class ProductResponse {
    List<ProductDTO> products;

    public ProductResponse() {
    }

    public ProductResponse(List<ProductDTO> products) {
        this.products = products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
