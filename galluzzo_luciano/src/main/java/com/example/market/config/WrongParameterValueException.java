package com.example.market.config;

public class WrongParameterValueException extends Exception{

    public WrongParameterValueException(String paramName, String paramValue){

        super("Invalid value: '" + paramValue + "' for parameter: '" + paramName + "'");
    }
}
