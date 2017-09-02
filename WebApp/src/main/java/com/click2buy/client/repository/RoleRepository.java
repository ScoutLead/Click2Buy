package com.click2buy.client.repository;

import com.click2buy.client.model.Role;
import com.click2buy.client.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(RoleType role);
}
