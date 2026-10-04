package com.cth.job.service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "maker_checker_request")
public class MakerCheckerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_type", nullable = false, length = 50)
    private String requestType;

    @Column(name = "maker_user", nullable = false, length = 50)
    private String makerUser;

    @Column(name = "checker_user", length = 50)
    private String checkerUser;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "payload_json", length = 4000)
    private String payloadJson;

    @Column(length = 255)
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MakerCheckerRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }

    public String getMakerUser() { return makerUser; }
    public void setMakerUser(String makerUser) { this.makerUser = makerUser; }

    public String getCheckerUser() { return checkerUser; }
    public void setCheckerUser(String checkerUser) { this.checkerUser = checkerUser; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
