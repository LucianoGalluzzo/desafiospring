package com.example.market.config;

public class EmptyDataBaseException extends Exception{

    public EmptyDataBaseException(String file){
        super("Database in file : '" + file + "' is empty");
    }
}
