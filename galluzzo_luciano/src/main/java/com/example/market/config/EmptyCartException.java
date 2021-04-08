package com.example.market.config;

public class EmptyCartException extends Exception{

    public EmptyCartException(){
        super("No purchase request found");
    }
}
