package com.example.crm.controller;

import com.example.crm.dto.SellerDTO;
import com.example.crm.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @GetMapping
    public List<SellerDTO> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @GetMapping("/{id}")
    public SellerDTO getSellerById(@PathVariable Long id) {
        return sellerService.getSellerById(id);
    }

    @PostMapping
    public SellerDTO createSeller(@Valid @RequestBody SellerDTO sellerDTO) {
        return sellerService.createSeller(sellerDTO);
    }

    @PutMapping("/{id}")
    public SellerDTO updateSeller(@PathVariable Long id, @Valid @RequestBody SellerDTO sellerDTO) {
        return sellerService.updateSeller(id, sellerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}