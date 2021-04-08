package com.example.market.services;

import com.example.market.config.*;
import com.example.market.dtos.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MarketService {

    List<ArticleDTO> getProducts(Map<String, String> params) throws IllegalAmountArgumentException, WrongParameterValueException, IOException, WrongParameterException;
    List<ArticleDTO> getAllProducts() throws IOException;
    List<ArticleDTO> getProductsByOneOrTwoParams(Map<String, String> params) throws IOException, WrongParameterValueException;
    List<ArticleDTO> sortList(List<ArticleDTO> list, int order) throws WrongParameterValueException;
    void updateDataBase(List<ArticleDTO> listArticles) throws IOException;
    ResponseDTO purchaseRequest(PayloadDTO payload) throws IOException, InsufficientStockException, NotFoundArticleException;
    void validateParams(Map<String, String> params) throws WrongParameterException;
    void addToCart(TicketDTO ticket);
    CartDTO getCart() throws EmptyCartException;
}
