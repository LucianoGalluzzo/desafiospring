package com.example.market.controllers;

import com.example.market.config.IllegalAmountArgumentException;
import com.example.market.dtos.ArticleDTO;
import com.example.market.dtos.ErrorDTO;
import com.example.market.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
public class MarketController {

    @Autowired
    private MarketService marketService;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getProducts(@RequestParam Map<String, String> params) throws IllegalAmountArgumentException {
        return new ResponseEntity<>(marketService.getProducts(params), HttpStatus.OK);
    }

    @ExceptionHandler(value={IllegalAmountArgumentException.class})
    public ResponseEntity<ErrorDTO> illegalAmountArgumentException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Invalid Parameters", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

}
