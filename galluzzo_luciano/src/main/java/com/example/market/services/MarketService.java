package com.example.market.services;

import com.example.market.config.*;
import com.example.market.dtos.ArticleDTO;
import com.example.market.dtos.PayloadDTO;
import com.example.market.dtos.ResponseDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MarketService {

    List<ArticleDTO> getProducts(Map<String, String> params) throws IllegalAmountArgumentException, WrongOrderException, IOException, WrongParameterException;
    List<ArticleDTO> getAllProducts() throws IOException;
    List<ArticleDTO> getProductsByOneOrTwoParams(Map<String, String> params) throws IOException;
    List<ArticleDTO> sortList(List<ArticleDTO> list, int order) throws WrongOrderException;
    void updateDataBase(List<ArticleDTO> listArticles);
    ResponseDTO purchaseRequest(PayloadDTO payload) throws IOException, InsufficientStockException, NotFoundArticleException;
    void validateParams(Map<String, String> params) throws WrongParameterException;
}
