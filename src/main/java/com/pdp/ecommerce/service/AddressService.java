package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

    void save(Address address);
}
