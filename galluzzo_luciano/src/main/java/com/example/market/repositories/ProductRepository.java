package com.example.market.repositories;

import com.example.market.dtos.ArticleDTO;

import java.util.List;

public interface ProductRepository {

    void loadDataBase();
    List<ArticleDTO> getAllProducts();
    List<ArticleDTO> getProductsByCategory(List<ArticleDTO> list, String categoryName);
    List<ArticleDTO> getProductsByName(List<ArticleDTO> list, String name);
    List<ArticleDTO> getProductsByBrand(List<ArticleDTO> list, String brand);
    List<ArticleDTO> getProductsByPrice(List<ArticleDTO> list, double price);
    List<ArticleDTO> getProductsByFreeShipping(List<ArticleDTO> list, boolean freeShipping);
    List<ArticleDTO> getProductsByPrestige(List<ArticleDTO> list, int prestige);
}
