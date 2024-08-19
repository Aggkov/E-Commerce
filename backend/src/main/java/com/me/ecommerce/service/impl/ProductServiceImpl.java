package com.me.ecommerce.service.impl;

import com.me.ecommerce.dto.response.PagedResponse;
import com.me.ecommerce.dto.response.ProductDTO;
import com.me.ecommerce.entity.Product;
import com.me.ecommerce.mapper.ProductMapper;
import com.me.ecommerce.repository.ProductRepository;
import com.me.ecommerce.service.ProductService;
import com.me.ecommerce.utils.AppConstants;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import static com.me.ecommerce.utils.AppConstants.CREATED_AT;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public PagedResponse<ProductDTO> getAllProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, CREATED_AT);

        Page<Product> products = productRepository.findAll(pageable);

        if (products.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(),
                    products.getNumber(), products.getSize(),
                    products.getTotalElements(),
                    products.getTotalPages()
//                    , productListPage.isLast()
            );
        }
        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();
        return new PagedResponse<>(productDTOS, products.getNumber(),
                products.getSize(), products.getTotalElements(),
                products.getTotalPages()
//               , albums.isLast()
        );
    }

    @Override
    public PagedResponse<ProductDTO> getProductsByCategory(Long id, Integer page ,Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC);

        Page<Product> productsByCategory = productRepository.findByCategoryId(id, pageable);

        List<ProductDTO> productDTOS = productsByCategory.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();
        return new PagedResponse<>(productDTOS, productsByCategory.getNumber(),
                productsByCategory.getSize(), productsByCategory.getTotalElements(),
                productsByCategory.getTotalPages()
//               , albums.isLast()
        );
    }

    @Override
    public PagedResponse<ProductDTO> searchProductByKeywordsPaginated(String keywords, Integer page ,Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC);

        Page<Product> productsByKeywords = productRepository.searchByKeywordsPaginated(keywords, pageable);

        List<ProductDTO> productDTOS = productsByKeywords.getContent().stream()
                .map(productMapper::productToProductDTO)
                .toList();
        return new PagedResponse<>(productDTOS, productsByKeywords.getNumber(),
                productsByKeywords.getSize(), productsByKeywords.getTotalElements(),
                productsByKeywords.getTotalPages()
//               , albums.isLast()
        );
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::productToProductDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product product) {
        // Find the existing product by ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update the existing product with new values
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setUnitPrice(product.getUnitPrice());
        // Update other fields as necessary

        // Save and return the updated product
        return productRepository.save(existingProduct);
    }

    @Override
    public List<ProductDTO> searchProductByKeywords(String keywords) {
        List<Product> products = productRepository.searchByKeywords(keywords);
        return products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
    }
}

