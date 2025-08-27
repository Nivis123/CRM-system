package com.example.crm.service;

import com.example.crm.dto.SellerDTO;
import com.example.crm.entity.Seller;
import com.example.crm.exception.ResourceNotFoundException;
import com.example.crm.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    public List<SellerDTO> getAllSellers() {
        return sellerRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public SellerDTO getSellerById(Long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        return convertToDTO(seller);
    }

    public SellerDTO createSeller(SellerDTO sellerDTO) {
        Seller seller = convertToEntity(sellerDTO);
        seller = sellerRepository.save(seller);
        return convertToDTO(seller);
    }

    public SellerDTO updateSeller(Long id, SellerDTO sellerDTO) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        seller.setName(sellerDTO.getName());
        seller.setContactInfo(sellerDTO.getContactInfo());
        seller = sellerRepository.save(seller);
        return convertToDTO(seller);
    }

    public void deleteSeller(Long id) {
        Seller seller = sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        sellerRepository.delete(seller);
    }

    private SellerDTO convertToDTO(Seller seller) {
        SellerDTO dto = new SellerDTO();
        dto.setId(seller.getId());
        dto.setName(seller.getName());
        dto.setContactInfo(seller.getContactInfo());
        dto.setRegistrationDate(seller.getRegistrationDate());
        return dto;
    }

    private Seller convertToEntity(SellerDTO dto) {
        Seller seller = new Seller();
        seller.setName(dto.getName());
        seller.setContactInfo(dto.getContactInfo());
        return seller;
    }
}