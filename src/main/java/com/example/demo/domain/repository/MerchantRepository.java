package com.example.demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.entity.Merchant;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {

}
