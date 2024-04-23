package com.mockcompany.webapp.controller;

import com.mockcompany.webapp.api.SearchResponse;
import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final ProductItemRepository productItemRepository;

    @Autowired
    public SearchController(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    @GetMapping
    public SearchResponse search(@RequestParam(name = "q", required = false) String query) {
        List<ProductItem> productItems;

        if (query == null || query.trim().isEmpty()) {
            productItems = productItemRepository.findAll();
        } else {
            productItems = productItemRepository.searchByName(query);
        }

        List<SearchResponse.ProductInfo> productInfos = productItems.stream()
                .map(productItem -> new SearchResponse.ProductInfo(
                        productItem.getId(),
                        productItem.getName(),
                        productItem.getPrice(),
                        productItem.getType(),
                        productItem.getBrand(),
                        productItem.getColor(),
                        productItem.getSize()
                ))
                .collect(Collectors.toList());

        return new SearchResponse(productInfos);
    }
}