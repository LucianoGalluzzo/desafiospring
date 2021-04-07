package com.example.market.controllers;

import com.example.market.config.*;
import com.example.market.dtos.ArticleDTO;
import com.example.market.dtos.ErrorDTO;
import com.example.market.dtos.PayloadDTO;
import com.example.market.dtos.ResponseDTO;
import com.example.market.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    public ResponseEntity<List<ArticleDTO>> getProducts(@RequestParam Map<String, String> params) throws IllegalAmountArgumentException, WrongOrderException, IOException, WrongParameterException {
        return new ResponseEntity<>(marketService.getProducts(params), HttpStatus.OK);
    }

    @PostMapping("/purchase-request")
    public ResponseEntity<ResponseDTO> purchaseRequest(@RequestBody PayloadDTO payload) throws InsufficientStockException, IOException, NotFoundArticleException {
        return new ResponseEntity<>(marketService.purchaseRequest(payload), HttpStatus.OK);
    }

    @ExceptionHandler(value={IllegalAmountArgumentException.class})
    public ResponseEntity<ErrorDTO> illegalAmountArgumentException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Invalid Parameters", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={WrongOrderException.class})
    public ResponseEntity<ErrorDTO> wrongOrderException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Invalid order parameter", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={FileNotFoundException.class})
    public ResponseEntity<ErrorDTO> fileNotFoundException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("File not found", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value={IOException.class})
    public ResponseEntity<ErrorDTO> IOException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Error in file reading", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value={NotFoundArticleException.class})
    public ResponseEntity<ErrorDTO> notFoundArticleException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Article not found in database", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={InsufficientStockException.class})
    public ResponseEntity<ErrorDTO> insufficientStockException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Insufficient stock", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={WrongParameterException.class})
    public ResponseEntity<ErrorDTO> wrongParameterException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Wrong parameter", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
