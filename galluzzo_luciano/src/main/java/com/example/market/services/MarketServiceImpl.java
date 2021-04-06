package com.example.market.services;

import com.example.market.config.IllegalAmountArgumentException;
import com.example.market.dtos.ArticleDTO;
import com.example.market.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MarketServiceImpl implements MarketService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ArticleDTO> getProducts(Map<String, String> params) throws IllegalAmountArgumentException {
        int order=9999, size;
        boolean needOrder=false;
        List<ArticleDTO> list = new ArrayList<>();

        if(params.containsKey("order")){
            int orden = Integer.parseInt(params.get("order"));
            params.remove("order");
            needOrder = true;
        }

        size = params.size();
        if(params.size() > 2){
            throw new IllegalAmountArgumentException("There are more than 2 filters parameters");
        }else{
            if (size == 0) {
                list = getAllProducts();
            } else {
                list = getProductsByOneOrTwoParams(params);
            }
        }

        if(needOrder){
            list = sortList(list, order);
        }

        return list;
    }

    @Override
    public List<ArticleDTO> getAllProducts() {
        return productRepository.getAllProducts();
    }


    @Override
    public List<ArticleDTO> getProductsByOneOrTwoParams(Map<String, String> params) {

        List<ArticleDTO> list = productRepository.getAllProducts();

        if(params.containsKey("category")){
            list = productRepository.getProductsByCategory(list, params.get("category"));
        }
        if(params.containsKey("name")){
            list = productRepository.getProductsByName(list, params.get("name"));
        }
        if(params.containsKey("brand")){
            list = productRepository.getProductsByBrand(list, params.get("brand"));
        }
        if(params.containsKey("price")){
            double price = Double.parseDouble(params.get("price"));
            list = productRepository.getProductsByPrice(list, price);
        }
        if(params.containsKey("freeShipping")){
            boolean freeShipping = false;
            if(params.get("freeShipping").equals("true")){
                freeShipping = true;
            }
            list = productRepository.getProductsByFreeShipping(list, freeShipping);
        }
        if(params.containsKey("prestige")){
            int prestige = Integer.parseInt(params.get("prestige"));
            list = productRepository.getProductsByPrestige(list, prestige);
        }
        return list;
    }

    @Override
    public List<ArticleDTO> sortList(List<ArticleDTO> list, int order) {

        Comparator<Double> c2 = (a,b) -> (int) (a - b);
        list.sort((Comparator) c2);
        return list;
    }
}
