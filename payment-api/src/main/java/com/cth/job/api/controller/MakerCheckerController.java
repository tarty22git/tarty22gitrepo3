package com.cth.job.api.controller;

import com.cth.job.core.dto.PaymentRequest;
import com.cth.job.service.entity.MakerCheckerRequest;
import com.cth.job.service.service.MakerCheckerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
@Tag(name = "Maker-Checker Operations", description = "Endpoints for initiating and approving/rejecting payment transactions")
public class MakerCheckerController {

    private final MakerCheckerService makerCheckerService;

    @Autowired
    public MakerCheckerController(MakerCheckerService makerCheckerService) {
        this.makerCheckerService = makerCheckerService;
    }

    @PostMapping("/submit-payment")
    @Operation(summary = "Maker submits a payment request")
    public ResponseEntity<MakerCheckerRequest> submitPayment(@RequestBody PaymentRequest request, Authentication authentication) {
        String makerUser = authentication != null ? authentication.getName() : "maker";
        MakerCheckerRequest mcReq = makerCheckerService.submitPaymentRequest(request, makerUser);
        return ResponseEntity.ok(mcReq);
    }

    @GetMapping("/pending")
    @Operation(summary = "List all pending requests requiring approval")
    public ResponseEntity<List<MakerCheckerRequest>> getPendingRequests() {
        return ResponseEntity.ok(makerCheckerService.getPendingRequests());
    }

    @PostMapping("/requests/{id}/approve")
    @Operation(summary = "Checker approves a pending request")
    public ResponseEntity<MakerCheckerRequest> approveRequest(@PathVariable("id") Long id,
                                                              @RequestParam(value = "remarks", defaultValue = "Approved") String remarks,
                                                              Authentication authentication) {
        String checkerUser = authentication != null ? authentication.getName() : "checker";
        MakerCheckerRequest mcReq = makerCheckerService.approveRequest(id, checkerUser, remarks);
        return ResponseEntity.ok(mcReq);
    }

    @PostMapping("/requests/{id}/reject")
    @Operation(summary = "Checker rejects a pending request")
    public ResponseEntity<MakerCheckerRequest> rejectRequest(@PathVariable("id") Long id,
                                                             @RequestParam(value = "remarks", defaultValue = "Rejected") String remarks,
                                                             Authentication authentication) {
        String checkerUser = authentication != null ? authentication.getName() : "checker";
        MakerCheckerRequest mcReq = makerCheckerService.rejectRequest(id, checkerUser, remarks);
        return ResponseEntity.ok(mcReq);
    }
}
