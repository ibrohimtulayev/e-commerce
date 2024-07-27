package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Address;
import com.pdp.ecommerce.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service

@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    @Override
    public void save(Address address) {
        addressRepository.save(address);
    }
}
