package com.example.market.config;

public class NotFoundArticleException extends Exception{

    public NotFoundArticleException(Long id, String name, String brand){
        super("Article with id '" + id.toString() + "', name '" + name + "', and brand '" + brand + "' not found in database");
    }

}
