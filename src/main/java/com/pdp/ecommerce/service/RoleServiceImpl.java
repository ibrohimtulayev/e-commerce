package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Role;
import com.pdp.ecommerce.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
    private final RoleRepository roleRepository;
    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }
}
