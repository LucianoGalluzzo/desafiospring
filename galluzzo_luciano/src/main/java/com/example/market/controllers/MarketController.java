package com.example.market.controllers;

import com.example.market.config.*;
import com.example.market.dtos.*;
import com.example.market.services.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
public class MarketController {

    @Autowired
    private MarketService marketService;

    /*
    El siguiente endpoint devuelve la lista de todos los productos que se encuentran en la base de datos (archivo)
    Si se pasan más de 2 parámetros, algún parametro invalido o un orden que no corresponde devuelve la excepcion
    correspondiente
     */
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getProducts(@RequestParam Map<String, String> params) throws IllegalAmountArgumentException, WrongParameterValueException, IOException, WrongParameterException {
        return new ResponseEntity<>(marketService.getProducts(params), HttpStatus.OK);
    }

    /*
    El siguiente endpoint recibe un objeto PayloadDTO que contiene una lista de articulos
    Devuelve un objeto ResponseDTO que tiene un Ticket y un Status. Si la cantidad solicitada es mayor al
    stock disponible devuelve una excepcion. Tambien arroja una excepcion cuando se solicita un producto que
    no existe, verificando el productId, nombre y marca (teniendo en cuenta que más de un producto tiene el mismo
    nombre pero se diferencia por la marca).
    Estas solicitudes son agregadas al carrito de compras
    */
    @PostMapping("/purchase-request")
    public ResponseEntity<ResponseDTO> purchaseRequest(@RequestBody PayloadDTO payload) throws InsufficientStockException, IOException, NotFoundArticleException {
        return new ResponseEntity<>(marketService.purchaseRequest(payload), HttpStatus.OK);
    }

    /*
    El siguiente endpoint devuelve el carrito de compras completo, que contiene una lista de tickets y un total
    acumulado. Devuelve una excepcion en caso de que el carrito este vacío y no se encuentren solicitudes de compra
    El carrito contiene las solicitudes de compra enviadas en el endpoint 'purchase-request'
     */
    @GetMapping("/cart")
    public ResponseEntity<CartDTO> getCart() throws EmptyCartException {
        return new ResponseEntity<>(marketService.getCart(), HttpStatus.OK);
    }

    @ExceptionHandler(value={IllegalAmountArgumentException.class})
    public ResponseEntity<ErrorDTO> illegalAmountArgumentException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Invalid Parameters", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value={WrongParameterValueException.class})
    public ResponseEntity<ErrorDTO> wrongOrderException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Invalid parameter value", e.getMessage());
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

    @ExceptionHandler(value={EmptyCartException.class})
    public ResponseEntity<ErrorDTO> emptyCartException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Empty Cart", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
