package com.example.market.repositories;

import com.example.market.dtos.ClientDTO;

import java.io.IOException;
import java.util.List;

public interface ClientRepository {

    void loadDataBase() throws IOException;
    List<ClientDTO> getAllClients() throws IOException;
    List<ClientDTO> getClientsByProvince(String province) throws IOException;
    boolean existedClient(ClientDTO client) throws IOException;
    void addClient(ClientDTO client) throws IOException;
    void saveDB() throws IOException;

}
