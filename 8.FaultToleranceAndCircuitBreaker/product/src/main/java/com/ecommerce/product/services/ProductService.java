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
import java.util.Optional;

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

        System.out.println("ProductRequest :"+productRequest);

        Product product = modelMapper.map(productRequest, Product.class);

        System.out.println("Product :"+product);

        product.setActive(true);
        Product saveProduct = productRepository.save(product);

        System.out.println("Product :"+product);

        ProductResponse productResponse = modelMapper.map(saveProduct, ProductResponse.class);

        System.out.println("ProductResponse :"+productResponse);

        return productResponse;
    }

    public ProductResponse updateProduct(String id, ProductRequest productRequest) {
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
        System.out.println("Product :"+product);

        List<ProductResponse> productResponses = product.stream().map(
                product1 -> modelMapper.map(product1, ProductResponse.class)
        ).toList();

        System.out.println("ProductResponse :"+productResponses);
        return productResponses;
    }

    public ProductResponse deleteProduct(String id) {
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

    public ProductResponse getProductById(String id) {
        Product product = (Product) productRepository.findByIdAndActiveTrue(id).
                orElseThrow(() -> new ResourceNotFoundException("Product","productId",Long.valueOf(id)));
        ProductResponse productResponse = modelMapper.map(product,ProductResponse.class);

        return productResponse;
    }
}
