package com.shopping.ECommerceWeb.controller;

import com.shopping.ECommerceWeb.model.Product;
import com.shopping.ECommerceWeb.service.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class productController {

    @Autowired
    productService service;

    @RequestMapping("/")
    public String greet(){
        return "hey buddy";
    }

    @RequestMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @RequestMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product=service.getProductById(id);
        if(product !=null){
            return new ResponseEntity<>(product,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        return new ResponseEntity<>(service.getProductById(id),HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart MultipartFile imageFile){
        System.out.println("hey biuddy");
        try{
            Product product1=service.addProduct(product,imageFile);
            return new ResponseEntity<>(product1,HttpStatus.CREATED);
        } catch (Exception e) {
        System.out.println("hey biuddy2");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product product=service.getProductById(productId);
        byte[] imageFile=product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile){
        Product product1= null;
        try {
            product1 = service.updateProduct(id,product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);
        }
        if(product1!=null){
            return new ResponseEntity<>("Updated",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Failed to Update",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product=service.getProductById(id);
        if(product !=null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        List<Product> products=service.searchProducts(keyword);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
}
