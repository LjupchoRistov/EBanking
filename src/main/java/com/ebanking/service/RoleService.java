package com.ebanking.service;

import com.ebanking.models.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role findByName(String name);

    List<Role> removeRole(Long roleId, List<Role> roles);
}
