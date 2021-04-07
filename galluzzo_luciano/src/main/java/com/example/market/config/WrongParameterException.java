package com.example.market.config;

public class WrongParameterException extends Exception{

    public WrongParameterException(String param){
        super("The following param: '" + param + "' is not a valid param name");
    }
}
