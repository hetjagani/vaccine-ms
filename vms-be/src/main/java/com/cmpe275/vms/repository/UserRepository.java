package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
