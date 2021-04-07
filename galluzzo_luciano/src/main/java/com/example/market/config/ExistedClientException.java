package com.example.market.config;

public class ExistedClientException extends Exception{

    public ExistedClientException(String dni){
        super("Client with dni '" + dni + "' already existed");
    }
}
