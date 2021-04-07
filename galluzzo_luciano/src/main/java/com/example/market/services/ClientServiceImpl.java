package com.example.market.services;

import com.example.market.config.ExistedClientException;
import com.example.market.config.MissingFieldsClientException;
import com.example.market.dtos.ClientDTO;
import com.example.market.repositories.ClientRepository;
import com.example.market.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    private AtomicLong idTicket = new AtomicLong(1);

    @Override
    public List<ClientDTO> getClients(String province) throws IOException {
        if(province.equals("")){
            return clientRepository.getAllClients();
        }else{
            return clientRepository.getClientsByProvince(province);
        }
    }

    @Override
    public void createClient(ClientDTO client) throws ExistedClientException, MissingFieldsClientException, IOException {
        if(client.getProvince() == null || client.getProvince().equals("") ||
            client.getDni() == null || client.getDni().equals("") ||
            client.getName() == null || client.getName().equals("") ||
            client.getSurname() == null || client.getSurname().equals(""))
            throw new MissingFieldsClientException("Some client information is missed");
        if(clientRepository.existedClient(client))
            throw new ExistedClientException(client.getDni());

        clientRepository.addClient(client);
    }
}
