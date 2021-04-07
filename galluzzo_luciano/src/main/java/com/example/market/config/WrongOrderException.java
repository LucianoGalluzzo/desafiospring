package com.example.market.config;

public class WrongOrderException extends Exception{

    public WrongOrderException(String msj){
        super(msj);
    }
}
