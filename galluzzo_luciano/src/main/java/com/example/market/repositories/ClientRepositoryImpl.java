package com.example.market.repositories;

import com.example.market.dtos.ArticleDTO;
import com.example.market.dtos.ClientDTO;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ClientRepositoryImpl implements ClientRepository{

    private boolean dataBaseLoaded = false;
    private final List<ClientDTO> dataBase = new ArrayList<>();
    private final AtomicLong clientId = new AtomicLong(1);
    private final String csvFile = "src/main/resources/dbClients.csv";


    @Override
    public void loadDataBase() throws IOException {
        if(!dataBaseLoaded){
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            try {
                br = new BufferedReader(new FileReader(csvFile));
                line = br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);
                    dataBase.add(new ClientDTO(clientId.getAndIncrement(), data[0], data[1], data[2], data[3]));
                }
                dataBaseLoaded = true;
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("File dbClients.csv not found");
            } catch (IOException e) {
                throw new IOException("Error reading the following file: dbClients.csv");
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new IOException("Error closing the following file: dbClients.csv");
                    }
                }
            }
        }
    }

    @Override
    public List<ClientDTO> getAllClients() throws IOException {
        loadDataBase();
        return dataBase;
    }

    @Override
    public List<ClientDTO> getClientsByProvince(String province) throws IOException {
        loadDataBase();
        return dataBase.stream().filter(clientDTO -> clientDTO.getProvince().equals(province)).
                collect(Collectors.toList());
    }

    @Override
    public boolean existedClient(ClientDTO client) throws IOException {
        loadDataBase();
        return dataBase.contains(client);
    }

    public void addClient(ClientDTO client) throws IOException {
        loadDataBase();
        client.setClientId(clientId.getAndIncrement());
        dataBase.add(client);
        saveDB();
    }

    public void saveDB() throws IOException {
        FileWriter writer = new FileWriter(csvFile);
        String separator = ",";
        String collect = "DNI,Name,Surname,Province\n";
        for(ClientDTO clientDTO: dataBase) {
            collect += clientDTO.getDni() + separator + clientDTO.getName() + separator + clientDTO.getSurname() + separator + clientDTO.getProvince() + "\n";
        }
        writer.write(collect);
        writer.close();
    }
}
