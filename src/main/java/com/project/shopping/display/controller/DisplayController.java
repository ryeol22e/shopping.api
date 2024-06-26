package com.project.shopping.display.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopping.display.dto.BannerInfo;
import com.project.shopping.display.service.DisplayService;
import com.project.shopping.product.dto.ProductInfo;
import com.project.shopping.product.service.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/display")
public class DisplayController {
    private final DisplayService displayService;
    private final ProductService productService;

    @GetMapping("/main/banner")
    public ResponseEntity<List<BannerInfo>> getMainBanner(BannerInfo bannerDTO) {
        return ResponseEntity.ok(displayService.getBannerList(bannerDTO));
    }

    @GetMapping("/main/corner")
    public ResponseEntity<String> getMainCorner() {
        return ResponseEntity.ok("");
    }

    @GetMapping("/product/list")
    public ResponseEntity<List<ProductInfo>> getProductList(ProductInfo productDTO) {
        return ResponseEntity.ok(productService.getProductList(productDTO));
    }
}
