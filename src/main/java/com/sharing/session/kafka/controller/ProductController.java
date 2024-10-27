package com.sharing.session.kafka.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharing.session.kafka.constant.ApiPath;
import com.sharing.session.kafka.entity.Product;
import com.sharing.session.kafka.helper.ResponseHelper;
import com.sharing.session.kafka.model.request.ProductRequest;
import com.sharing.session.kafka.model.response.WebResponse;
import com.sharing.session.kafka.service.ProductService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.PRODUCTS)
public class ProductController {
  private final ProductService productService;

  @PostMapping
  public Mono<WebResponse<Product>> createProduct(@RequestBody ProductRequest request) {
    return this.productService.createProduct(request).map(ResponseHelper::ok);
  }

  @GetMapping
  public Mono<WebResponse<List<Product>>> getAllProducts() {
    return this.productService.getAllProducts().map(ResponseHelper::ok);
  }

  @PutMapping(ApiPath.ID)
  public Mono<WebResponse<Product>> updateProduct(@PathVariable String id,
      @RequestBody ProductRequest product) {
    return this.productService.updateProduct(id, product).map(ResponseHelper::ok);
  }

  @DeleteMapping(ApiPath.ID)
  public Mono<WebResponse<Void>> deleteProduct(@PathVariable String id) {
    return this.productService.deleteProduct(id).thenReturn(ResponseHelper.ok());
  }
}
