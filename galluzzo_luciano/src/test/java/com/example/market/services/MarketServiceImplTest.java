package com.example.market.services;


import com.example.market.config.*;
import com.example.market.dtos.*;
import com.example.market.repositories.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class MarketServiceImplTest {

    @InjectMocks
    MarketServiceImpl marketService;

    @Mock
    private ProductRepository productRepository;

    private static List<ArticleDTO> articles, articles2, articlesForPurchase, articlesForPurchaseError;
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

        articlesForPurchaseError =
                objectMapper.readValue(new File("src/main/resources/mockArticlesForPurchaseError.json"),
                        new TypeReference<>() {
                        });
    }

    @BeforeEach
    void initMocks() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getProductsTest() throws IOException, EmptyDataBaseException, IllegalAmountArgumentException, WrongParameterValueException, WrongParameterException {

        when(productRepository.getAllProducts()).thenReturn(articles);

        List<ArticleDTO> responseArticles = marketService.getProducts(new HashMap<>());

        Assertions.assertEquals(articles, responseArticles);
    }

    @Test
    void getProductsFilterByCategoryTest() throws IOException, EmptyDataBaseException, IllegalAmountArgumentException, WrongParameterValueException, WrongParameterException {

        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        Map<String, String> params = new HashMap<>();
        params.put("category", "Herramientas");

        when(productRepository.getAllProducts()).thenReturn(articlesTest);
        articlesTest = articlesTest.stream().filter(articleDTO -> articleDTO.getCategory().equals("Herramientas"))
                .collect(Collectors.toList());
        when(productRepository.getProductsByCategory(any(), any())).thenReturn(articlesTest);


        List<ArticleDTO> responseArticles = marketService.getProducts(params);

        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void getProductsFilterByCategoryAndShippingTest() throws IOException, EmptyDataBaseException, IllegalAmountArgumentException, WrongParameterValueException, WrongParameterException {

        List<ArticleDTO> articles2Test = new ArrayList<>(articles2);
        Map<String, String> params = new HashMap<>();
        params.put("category", "Herramientas");
        params.put("freeShipping", "true");

        when(productRepository.getAllProducts()).thenReturn(articles2Test);
        articles2Test = articles2Test.stream().filter(articleDTO -> articleDTO.getCategory().equals("Herramientas")).
                collect(Collectors.toList());
        when(productRepository.getProductsByCategory(any(), any())).thenReturn(articles2Test);
        List<ArticleDTO> articles3Test = articles2Test.stream().filter(ArticleDTO::isFreeShipping).
                collect(Collectors.toList());
        when(productRepository.getProductsByFreeShipping(articles2Test, true)).thenReturn(articles3Test);

        List<ArticleDTO> responseArticles = marketService.getProducts(params);

        Assertions.assertEquals(articles3Test, responseArticles);


    }

    @Test
    void getProductsOrderAscTest() throws Exception{
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        Map<String, String> params = new HashMap<>();
        params.put("order", "0");

        when(productRepository.getAllProducts()).thenReturn(articlesTest);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getName));

        List<ArticleDTO> responseArticles = marketService.getProducts(params);

        Assertions.assertEquals(articlesTest, responseArticles);

    }

    @Test
    void getProductsOrderDescTest() throws Exception{
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        Map<String, String> params = new HashMap<>();
        params.put("order", "1");

        when(productRepository.getAllProducts()).thenReturn(articlesTest);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getName).reversed());

        List<ArticleDTO> responseArticles = marketService.getProducts(params);

        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void getProductsPriceMayTest() throws Exception{
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        Map<String, String> params = new HashMap<>();
        params.put("order", "2");

        when(productRepository.getAllProducts()).thenReturn(articlesTest);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getPrice).reversed());

        List<ArticleDTO> responseArticles = marketService.getProducts(params);

        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void getProductsPriceMinTest() throws Exception{
        List<ArticleDTO> articlesTest = new ArrayList<>(articles);
        Map<String, String> params = new HashMap<>();
        params.put("order", "3");

        when(productRepository.getAllProducts()).thenReturn(articlesTest);
        articlesTest.sort(Comparator.comparing(ArticleDTO::getPrice));

        List<ArticleDTO> responseArticles = marketService.getProducts(params);

        Assertions.assertEquals(articlesTest, responseArticles);
    }

    @Test
    void purchaseRequestOKTest() throws Exception {
        ResponseDTO responseTest = purchaseRequestFixture();
        PayloadDTO payloadTest = payloadFixture();
        when(productRepository.getAllProducts()).thenReturn(articles);

        ResponseDTO actualResponse = marketService.purchaseRequest(payloadTest);

        Assertions.assertEquals(responseTest, actualResponse);
    }

    @Test
    void purchaseRequestArticleNotFoundTest() throws Exception {

        PayloadDTO payloadTest = payloadFixtureForError();

        when(productRepository.getAllProducts()).thenReturn(articles);

        Exception exception = Assertions.assertThrows(NotFoundArticleException.class, () -> {
            marketService.purchaseRequest(payloadTest);
        });

        String expectedMessage = "Article with id '92', name 'Cinturon', and brand 'Taverniti' not found in database";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    private PayloadDTO payloadFixture() {
        return new PayloadDTO(articlesForPurchase);
    }

    private PayloadDTO payloadFixtureForError() {
        return new PayloadDTO(articlesForPurchaseError);
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
