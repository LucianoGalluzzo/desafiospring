package com.example.market.services;

import com.example.market.config.*;
import com.example.market.dtos.*;
import com.example.market.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MarketServiceImpl implements MarketService{

    @Autowired
    private ProductRepository productRepository;

    private AtomicLong idTicket = new AtomicLong(1);
    private List<String> validParams = new ArrayList<String>(Arrays.asList(
            "category", "productId", "name", "brand", "price", "quantity", "freeShipping", "prestige"));

    @Override
    public List<ArticleDTO> getProducts(Map<String, String> params) throws IllegalAmountArgumentException, WrongOrderException, IOException, WrongParameterException {
        int order=9999, size;
        boolean needOrder=false;
        List<ArticleDTO> list = new ArrayList<>();

        if(params.containsKey("order")){
            order = Integer.parseInt(params.get("order"));
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
                validateParams(params);
                list = getProductsByOneOrTwoParams(params);
            }
        }

        if(needOrder){
            list = sortList(list, order);
        }

        return list;
    }

    @Override
    public List<ArticleDTO> getAllProducts() throws IOException {
        return productRepository.getAllProducts();
    }


    @Override
    public List<ArticleDTO> getProductsByOneOrTwoParams(Map<String, String> params) throws IOException {

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
    public List<ArticleDTO> sortList(List<ArticleDTO> list, int order) throws WrongOrderException {

        switch (order){
            case 0: list.sort(Comparator.comparing(ArticleDTO::getName));
            break;
            case 1: list.sort(Comparator.comparing(ArticleDTO::getName).reversed());
            break;
            case 2: list.sort(Comparator.comparing(ArticleDTO::getPrice));
            break;
            case 3: list.sort(Comparator.comparing(ArticleDTO::getPrice).reversed());
            break;
            default: throw new WrongOrderException("El orden " + String.valueOf(order) + " no es un tipo válido");
        }
        return list;
    }

    @Override
    public void updateDataBase(List<ArticleDTO> listArticles) {
        productRepository.updateDataBase(listArticles);
    }

    @Override
    public ResponseDTO purchaseRequest(PayloadDTO payload) throws IOException, InsufficientStockException, NotFoundArticleException {
        List<ArticleDTO> list = getAllProducts();
        List<ArticleResponseDTO> listArticleResponse = new ArrayList<>();
        ArticleDTO aux = null;
        double total = 0;
        for(ArticleDTO a:payload.getArticles()){
            if(list.contains(a)){
                aux = list.get(list.indexOf(a));
                if(a.getQuantity() > aux.getQuantity())
                    throw new InsufficientStockException(a.getName());
                total += a.getQuantity() * aux.getPrice();
                listArticleResponse.add(new ArticleResponseDTO(a));
            }else
                throw new NotFoundArticleException(a.getProductId(), a.getName());
        }

        updateDataBase(payload.getArticles());

        TicketDTO ticket = new TicketDTO(idTicket.getAndIncrement(), listArticleResponse, total);
        StatusDTO status = new StatusDTO(200, "La solicitud de compra se completó con éxito");
        return new ResponseDTO(ticket, status);
    }

    @Override
    public void validateParams(Map<String, String> params) throws WrongParameterException {
        for(Map.Entry<String, String> entry:params.entrySet()){
            if(!validParams.contains(entry.getKey())){
                throw new WrongParameterException(entry.getKey());
            }
        }
    }
}
