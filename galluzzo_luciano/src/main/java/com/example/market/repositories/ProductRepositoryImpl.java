package com.example.market.repositories;

import com.example.market.dtos.ArticleDTO;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository{

    private boolean dataBaseLoaded = false;
    private final List<ArticleDTO> dataBase = new ArrayList();
    private final AtomicLong productId = new AtomicLong(1);

    @Override
    public void loadDataBase() {
        if(!dataBaseLoaded){
            String csvFile = "src/main/resources/dbProductos.csv";
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";
            try {
                br = new BufferedReader(new FileReader(csvFile));
                line = br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(cvsSplitBy);
                    boolean freeShipping = data[5].toUpperCase().equals("SI")?true:false;
                    dataBase.add(new ArticleDTO(productId.getAndIncrement(), data[0], data[1], data[2],
                                    Double.parseDouble(data[3].replace("$", "").replace(".", "")), Integer.parseInt(data[4]), freeShipping,
                            data[6].length()));
                    }
                dataBaseLoaded = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public List<ArticleDTO> getAllProducts() {
        loadDataBase();
        return dataBase;
    }

    @Override
    public List<ArticleDTO> getProductsByCategory(List<ArticleDTO> list, String categoryName) {

        return list.stream().filter(articleDTO -> articleDTO.getCategory().equals(categoryName)).
                collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getProductsByName(List<ArticleDTO> list, String name) {
        return list.stream().filter(articleDTO -> articleDTO.getName().equals(name)).
                collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getProductsByBrand(List<ArticleDTO> list, String brand) {
        return list.stream().filter(articleDTO -> articleDTO.getBrand().equals(brand)).
                collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getProductsByPrice(List<ArticleDTO> list, double price) {
        return list.stream().filter(articleDTO -> articleDTO.getPrice() == price).
                collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getProductsByFreeShipping(List<ArticleDTO> list, boolean freeShipping) {
        return list.stream().filter(articleDTO -> articleDTO.isFreeShipping() == freeShipping).
                collect(Collectors.toList());
    }

    @Override
    public List<ArticleDTO> getProductsByPrestige(List<ArticleDTO> list, int prestige) {
        return list.stream().filter(articleDTO -> articleDTO.getPrestige() == prestige).
                collect(Collectors.toList());
    }

}

