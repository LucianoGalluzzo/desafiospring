package com.example.market.repositories;

import com.example.market.config.EmptyDataBaseException;
import com.example.market.dtos.ArticleDTO;
import com.example.market.dtos.ClientDTO;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository{

    private boolean dataBaseLoaded = false;
    private final List<ArticleDTO> dataBase = new ArrayList<>();
    private final AtomicLong productId = new AtomicLong(1);
    private final String csvFile = "src/main/resources/dbProductos.csv";

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
                    boolean freeShipping = data[5].toUpperCase().equals("SI")?true:false;
                    dataBase.add(new ArticleDTO(productId.getAndIncrement(), data[0], data[1], data[2],
                                    Double.parseDouble(data[3].replace("$", "").replace(".", "")), Integer.parseInt(data[4]), freeShipping,
                            data[6]));
                    }
                dataBaseLoaded = true;
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("File " + csvFile + " not found");
            } catch (IOException e) {
                throw new IOException("Error reading the following file: " + csvFile);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new IOException("Error closing the following file: " + csvFile);
                    }
                }
            }
        }
    }

    @Override
    public List<ArticleDTO> getAllProducts() throws IOException, EmptyDataBaseException {
        loadDataBase();
        if(dataBase.isEmpty())
            throw new EmptyDataBaseException(csvFile);
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
        return list.stream().filter(articleDTO -> articleDTO.getPrestige().length() == prestige).
                collect(Collectors.toList());
    }

    @Override
    public void updateDataBase(List<ArticleDTO> listArticles) throws IOException {
        for(ArticleDTO a: listArticles){
            if(dataBase.contains(a)){
                dataBase.get(dataBase.indexOf(a)).decrementQuantity(a.getQuantity());
            }
        }
        saveDB();
    }

    public void saveDB() throws IOException {
        dataBase.sort(Comparator.comparing(ArticleDTO::getProductId));
        FileWriter writer = new FileWriter(csvFile);
        String separator = ",";
        String collect = "Producto,Categoría,Marca,Precio,Cantidad,Envío Gratis,Prestigio\n";
        String freeShipping = "NO";
        for(ArticleDTO articleDTO: dataBase) {
            if(articleDTO.isFreeShipping())
                freeShipping = "SI";
            else
                freeShipping = "NO";

            collect += articleDTO.getName() + separator + articleDTO.getCategory() + separator +
                    articleDTO.getBrand() + separator + "$" + (int)articleDTO.getPrice() + separator +
                    articleDTO.getQuantity() + separator + freeShipping + separator + articleDTO.getPrestige() +
                    "\n";
        }
        writer.write(collect);
        writer.close();
    }

}

