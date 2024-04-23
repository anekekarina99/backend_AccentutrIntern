package com.mockcompany.webapp.services;

import com.mockcompany.webapp.api.SearchResponse;
import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final ProductItemRepository productItemRepository;

    @Autowired
    public SearchService(ProductItemRepository productItemRepository) {
        this.productItemRepository = productItemRepository;
    }

    public SearchResponse performSearch(String query) {
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