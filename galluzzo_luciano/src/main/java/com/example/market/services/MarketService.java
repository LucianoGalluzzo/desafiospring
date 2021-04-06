package com.example.market.services;

import com.example.market.config.IllegalAmountArgumentException;
import com.example.market.dtos.ArticleDTO;

import java.util.List;
import java.util.Map;

public interface MarketService {

    List<ArticleDTO> getProducts(Map<String, String> params) throws IllegalAmountArgumentException;
    List<ArticleDTO> getAllProducts();
    List<ArticleDTO> getProductsByOneOrTwoParams(Map<String, String> params);
    List<ArticleDTO> sortList(List<ArticleDTO> list, int order);
}
