package com.app.ecom.controller;

import com.app.ecom.dto.ProductRequest;
import com.app.ecom.dto.ProductResponse;

import com.app.ecom.model.Product;
import com.app.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        return new ResponseEntity<ProductResponse>(productService.createProduct(productRequest),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProduct(){
        return new ResponseEntity<List<ProductResponse>>(productService.getProduct(),
                HttpStatus.OK);
    }

    @GetMapping("/recentlyDeletedProducts")
    public ResponseEntity<List<ProductResponse>> getDeletedProduct(){
        return new ResponseEntity<List<ProductResponse>>(productService.getDeletedProduct(),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest productRequest){
        return new ResponseEntity<ProductResponse>(productService.updateProduct(id,productRequest),
                HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id){
        ProductResponse deleteProductDTO = productService.deleteProduct(id);
        return new ResponseEntity<>("Product with productId: "+id +" is deleted successfully",HttpStatus.OK);

    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword){
        return new ResponseEntity<>(productService.searchProducts(keyword),HttpStatus.OK);
    }
}
