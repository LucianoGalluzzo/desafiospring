package com.example.market.controllers;

import com.example.market.config.ExistedClientException;
import com.example.market.config.MissingFieldsClientException;
import com.example.market.dtos.ClientDTO;
import com.example.market.dtos.ErrorDTO;
import com.example.market.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/v2")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> getClients(@RequestParam(value = "province", defaultValue = "") String province) throws IOException {
        return new ResponseEntity<>(clientService.getClients(province), HttpStatus.OK);
    }

    @PostMapping("/create-client")
    public ResponseEntity<String> createClient(@RequestBody ClientDTO clientDTO) throws MissingFieldsClientException, IOException, ExistedClientException {
        clientService.createClient(clientDTO);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
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

    @ExceptionHandler(value={ExistedClientException.class})
    public ResponseEntity<ErrorDTO> existedClientException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Existed Client", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value={MissingFieldsClientException.class})
    public ResponseEntity<ErrorDTO> missingFieldsClientException(Exception e){
        ErrorDTO errorDTO = new ErrorDTO("Insufficient parameters", e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }
}
