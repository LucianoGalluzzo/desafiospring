package com.example.market.services;

import com.example.market.config.ExistedClientException;
import com.example.market.config.MissingFieldsClientException;
import com.example.market.dtos.ClientDTO;

import java.io.IOException;
import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients(String province) throws IOException;
    void createClient(ClientDTO client) throws ExistedClientException, MissingFieldsClientException, IOException;
}
