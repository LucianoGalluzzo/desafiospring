package com.example.market.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDTO {

    private Long productId;
    private String name;
    private String brand;
    private int quantity;

    public ArticleResponseDTO(ArticleDTO articleDTO){
        this.productId = articleDTO.getProductId();
        this.name = articleDTO.getName();
        this.brand = articleDTO.getBrand();
        this.quantity = articleDTO.getQuantity();
    }

}
