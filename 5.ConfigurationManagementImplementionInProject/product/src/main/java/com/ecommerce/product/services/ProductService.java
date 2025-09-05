package com.ecommerce.product.services;


import com.ecommerce.product.dtos.ProductRequest;
import com.ecommerce.product.dtos.ProductResponse;
import com.ecommerce.product.exceptions.APIExceptions;
import com.ecommerce.product.exceptions.ResourceNotFoundException;
import com.ecommerce.product.models.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
//        accept the request and convert it into Model
//        Save the model
//        again convert Model into Response

        Product product = modelMapper.map(productRequest, Product.class);

        Product saveProduct = productRepository.save(product);

        ProductResponse productResponse = modelMapper.map(saveProduct, ProductResponse.class);

        return productResponse;
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        //First we need to check whether the data is present in Database or not , else throw exception
        Product saveproduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", id));

//      Convert DTO into Entity
        modelMapper.map(productRequest, saveproduct);

//        Save the product
        saveproduct = productRepository.save(saveproduct);

//        Convert entity to Dto
        ProductResponse productResponse = modelMapper.map(saveproduct, ProductResponse.class);

        return productResponse;

    }

    public List<ProductResponse> getProduct() {
        List<Product> product = productRepository.findByActiveTrue();

        List<ProductResponse> productResponses = product.stream().map(
                product1 -> modelMapper.map(product1, ProductResponse.class)
        ).toList();

        return productResponses;
    }

    public ProductResponse deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", id));

        product.setActive(false);
        productRepository.save(product);

        return modelMapper.map(product, ProductResponse.class);

    }

    public List<ProductResponse> getDeletedProduct() {
        List<Product> product = productRepository.findByActiveFalse();

        List<ProductResponse> productResponses = product.stream().map(
                product1 -> modelMapper.map(product1, ProductResponse.class)
        ).toList();

        return productResponses;
    }


    public List<ProductResponse> searchProducts(String keyword) throws ResourceNotFoundException {
        List<Product> product = productRepository.searchProducts(keyword);
        if (product.isEmpty()) {
            throw new APIExceptions("Product not found");
        }
        List<ProductResponse> productResponses = product.stream().map(
                product1 -> modelMapper.map(product1, ProductResponse.class)
        ).toList();

        return productResponses;
    }
}
