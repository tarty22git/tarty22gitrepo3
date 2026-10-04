package com.cth.job.service.repository;

import com.cth.job.service.entity.MakerCheckerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MakerCheckerRequestRepository extends JpaRepository<MakerCheckerRequest, Long> {
    List<MakerCheckerRequest> findByStatus(String status);
}
