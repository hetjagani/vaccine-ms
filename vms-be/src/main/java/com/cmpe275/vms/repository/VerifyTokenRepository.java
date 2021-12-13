package com.cmpe275.vms.repository;

import com.cmpe275.vms.model.VerifyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Integer> {
}
