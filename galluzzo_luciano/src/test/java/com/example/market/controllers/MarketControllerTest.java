package com.example.market.controllers;

import com.example.market.config.*;
import com.example.market.dtos.*;
import com.example.market.services.MarketService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MarketController.class)
public class MarketControllerTest {

    @MockBean
    MarketService marketService;

    @Autowired
    private MockMvc mockMvc;

    private static List<ArticleDTO> articles, articles2, articlesForPurchase;
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

        articlesForPurchase =
                objectMapper.readValue(new File("src/main/resources/mockArticlesForPurchase.json"),
                        new TypeReference<>() {
                        });

    }

    @Test
    void getProductsTest() throws Exception {

        //mock service
        when(marketService.getProducts(any())).thenReturn(articles);

        //get /articles
        //map into objects
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/articles"))
                .andExpect(status().isOk()).andReturn();

        List<ArticleDTO> responseArticles =
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {
                });

        //assert equals
        Assertions.assertEquals(articles, responseArticles);
    }

    @Test
    void getProductsFilterByCategoryTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("category", "Herramientas");
        List<ArticleDTO> articlesTest = articles.stream().filter(articleDTO -> articleDTO.getCategory().equals("Herramientas")).
                collect(Collectors.toList());
        when(marketService.getProducts(params)).thenReturn(articlesTest);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/articles?category=Herramientas"))
                .andExpect(status().isOk()).andReturn();

        List<ArticleDTO> responseArticles =
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {
                });

        //assert equals
        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void getProductsFilterByCategoryAndShippingTest() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("category", "Herramientas");
        params.put("freeShipping", "true");
        List<ArticleDTO> articles2Test = articles2.stream().filter(articleDTO -> articleDTO.getCategory().equals("Herramientas") &&
                articleDTO.isFreeShipping()).
                collect(Collectors.toList());
        when(marketService.getProducts(params)).thenReturn(articles2Test);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/articles?category=Herramientas&freeShipping=true"))
                .andExpect(status().isOk()).andReturn();

        List<ArticleDTO> responseArticles =
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {
                });

        //assert equals
        Assertions.assertEquals(articles2Test, responseArticles);
    }

    @Test
    void getProductsOrderAscTest() throws Exception {
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getName));

        when(marketService.getProducts(any())).thenReturn(articlesTest);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/articles?order=0"))
                .andExpect(status().isOk()).andReturn();

        List<ArticleDTO> responseArticles =
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {
                });

        //assert equals
        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void getProductsOrderDescTest() throws Exception {
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getName).reversed());

        when(marketService.getProducts(any())).thenReturn(articlesTest);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/articles?order=1"))
                .andExpect(status().isOk()).andReturn();

        List<ArticleDTO> responseArticles =
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {
                });

        //assert equals
        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void getProductsPriceMayTest() throws Exception {
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getPrice).reversed());

        when(marketService.getProducts(any())).thenReturn(articlesTest);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/articles?order=2"))
                .andExpect(status().isOk()).andReturn();

        List<ArticleDTO> responseArticles =
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {
                });

        //assert equals
        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void getProductsPriceMinTest() throws Exception {
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getPrice));

        when(marketService.getProducts(any())).thenReturn(articlesTest);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/articles?order=3"))
                .andExpect(status().isOk()).andReturn();

        List<ArticleDTO> responseArticles =
                objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {
                });

        //assert equals
        Assertions.assertEquals(articlesTest, responseArticles);
    }

    /*@Test
    void purchaseRequestOKTest() throws Exception {
        ResponseDTO responseTest = purchaseRequestFixture();
        PayloadDTO payloadTest = payloadFixture();
        when(marketService.purchaseRequest(any())).thenReturn(responseTest);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/purchase-request")
                .contentType(MediaType.APPLICATION_JSON).content(payloadTest))
                .andExpect(status().isOk()).andReturn();

        ResponseDTO actualResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<>() {});

        Assertions.assertEquals(responseTest, actualResponse);
    }*/

    private PayloadDTO payloadFixture() {
        return new PayloadDTO(articlesForPurchase);
    }

    ResponseDTO purchaseRequestFixture(){
        List<ArticleResponseDTO> articlesResponseForPurchase = new ArrayList<>();
        for (int i = 0; i < articlesForPurchase.size(); i++) {
            articlesResponseForPurchase.add(new ArticleResponseDTO(articlesForPurchase.get(i)));
        }
        TicketDTO ticketFixture = new TicketDTO(1L, articlesResponseForPurchase, 7100);
        StatusDTO statusFixture = new StatusDTO(200, "La solicitud de compra se completó con éxito");
        return new ResponseDTO(ticketFixture, statusFixture);
    }
}
