package com.example.market.repositories;

import com.example.market.config.EmptyDataBaseException;
import com.example.market.config.IllegalAmountArgumentException;
import com.example.market.config.WrongParameterException;
import com.example.market.config.WrongParameterValueException;
import com.example.market.dtos.ArticleDTO;
import com.example.market.services.MarketServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

public class ProductRepositoryImplTest {

    @InjectMocks
    ProductRepositoryImpl productRepository;

    @Mock
    ProductRepositoryImpl mockProductRepository;

    private static List<ArticleDTO> articles, articles2;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setUp() throws IOException {

        articles =
                objectMapper.readValue(new File("src/main/resources/mockArticles.json"),
                        new TypeReference<>() {
                        });

        articles2 =
                objectMapper.readValue(new File("src/main/resources/mockArticles2.json"),
                        new TypeReference<>() {
                        });
    }

    @BeforeEach
    void initMocks() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getProductsTest() throws IOException, EmptyDataBaseException {

        when(mockProductRepository.getAllProducts()).thenReturn(articles);

        List<ArticleDTO> responseArticles = productRepository.getAllProducts();

        Assertions.assertEquals(articles, responseArticles);

    }

    @Test
    void getProductsFilterByCategoryTest() throws IOException, EmptyDataBaseException {

        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        when(mockProductRepository.getAllProducts()).thenReturn(articles);

        articlesTest = articlesTest.stream().filter(articleDTO -> articleDTO.getCategory().equals("Herramientas"))
                .collect(Collectors.toList());

        List<ArticleDTO> responseArticles = productRepository.getProductsByCategory(articles, "Herramientas");

        Assertions.assertEquals(articlesTest, responseArticles);
    }

}
