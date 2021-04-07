package com.example.market.config;

public class InsufficientStockException extends Exception{

    public InsufficientStockException(String article){
        super("There are not stock for product: '" + article + "'");
    }
}
