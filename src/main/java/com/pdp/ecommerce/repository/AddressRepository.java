package com.pdp.ecommerce.repository;

import com.pdp.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}