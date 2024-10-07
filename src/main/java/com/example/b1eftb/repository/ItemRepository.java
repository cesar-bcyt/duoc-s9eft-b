package com.example.b1eftb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.b1eftb.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}