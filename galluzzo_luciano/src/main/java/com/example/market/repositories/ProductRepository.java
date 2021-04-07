package com.example.market.repositories;

import com.example.market.dtos.ArticleDTO;
import com.example.market.dtos.ClientDTO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ProductRepository {

    void loadDataBase() throws IOException;
    List<ArticleDTO> getAllProducts() throws IOException;
    List<ArticleDTO> getProductsByCategory(List<ArticleDTO> list, String categoryName);
    List<ArticleDTO> getProductsByName(List<ArticleDTO> list, String name);
    List<ArticleDTO> getProductsByBrand(List<ArticleDTO> list, String brand);
    List<ArticleDTO> getProductsByPrice(List<ArticleDTO> list, double price);
    List<ArticleDTO> getProductsByFreeShipping(List<ArticleDTO> list, boolean freeShipping);
    List<ArticleDTO> getProductsByPrestige(List<ArticleDTO> list, int prestige);
    void updateDataBase(List<ArticleDTO> listArticles) throws IOException;
    void saveDB() throws IOException;

}
