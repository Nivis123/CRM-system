package com.example.crm.service;

import com.example.crm.dto.TransactionDTO;
import com.example.crm.entity.Seller;
import com.example.crm.entity.Transaction;
import com.example.crm.exception.ResourceNotFoundException;
import com.example.crm.repository.SellerRepository;
import com.example.crm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return convertToDTO(transaction);
    }

    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        Seller seller = sellerRepository.findById(transactionDTO.getSellerId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        Transaction transaction = convertToEntity(transactionDTO);
        transaction.setSeller(seller);
        transaction = transactionRepository.save(transaction);
        return convertToDTO(transaction);
    }

    public List<TransactionDTO> getTransactionsBySellerId(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setSellerId(transaction.getSeller().getId());
        dto.setAmount(transaction.getAmount());
        dto.setPaymentType(transaction.getPaymentType());
        dto.setTransactionDate(transaction.getTransactionDate());
        return dto;
    }

    private Transaction convertToEntity(TransactionDTO dto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setPaymentType(dto.getPaymentType());
        return transaction;
    }
}