package com.example.market.config;

public class NotFoundArticleException extends Exception{

    public NotFoundArticleException(Long id, String name){
        super("Article with id '" + id.toString() + "' and name '" + name + "' not found in database");
    }
}
