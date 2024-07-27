package com.pdp.ecommerce.service;

import com.pdp.ecommerce.entity.Role;
import org.springframework.stereotype.Service;

@Service

public interface RoleService {
    void save(Role role);
}
