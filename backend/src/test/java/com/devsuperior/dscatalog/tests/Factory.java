package com.devsuperior.dscatalog.tests;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {

    public static Product creatProduct(){
        Product product = new Product(1L, "TV", "TV lg", 1200.0,
                "https://img.com/img.png", Instant.parse("2022-01-30T03:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDTO createProductDto(){
        Product product = creatProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static Category createCategory(){
        return new Category(1L, "Electronics");
    }
}
