package com.cth.job.api.controller;

import com.cth.job.service.model.GatewayStatusInfo;
import com.cth.job.service.registry.PaymentGatewayRegistry;
import com.cth.job.core.enums.PaymentProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateways")
@Tag(name = "Payment Gateways", description = "Endpoints to start, stop, scale, and inspect payment gateways")
public class PaymentGatewayController {

    private final PaymentGatewayRegistry gatewayRegistry;

    @Autowired
    public PaymentGatewayController(PaymentGatewayRegistry gatewayRegistry) {
        this.gatewayRegistry = gatewayRegistry;
    }

    @GetMapping
    @Operation(summary = "List all gateway statuses")
    public ResponseEntity<List<GatewayStatusInfo>> getGateways() {
        return ResponseEntity.ok(gatewayRegistry.getGatewayStatuses());
    }

    @PostMapping("/{provider}/start")
    @Operation(summary = "Start payment gateway")
    public ResponseEntity<String> startGateway(@PathVariable("provider") String providerStr) {
        PaymentProvider provider = PaymentProvider.valueOf(providerStr.toUpperCase());
        gatewayRegistry.startGateway(provider);
        return ResponseEntity.ok("Gateway " + provider + " STARTED successfully.");
    }

    @PostMapping("/{provider}/stop")
    @Operation(summary = "Stop payment gateway")
    public ResponseEntity<String> stopGateway(@PathVariable("provider") String providerStr) {
        PaymentProvider provider = PaymentProvider.valueOf(providerStr.toUpperCase());
        gatewayRegistry.stopGateway(provider);
        return ResponseEntity.ok("Gateway " + provider + " STOPPED successfully.");
    }

    @PostMapping("/{provider}/scale")
    @Operation(summary = "Scale gateway worker pool size")
    public ResponseEntity<String> scaleGatewayPool(@PathVariable("provider") String providerStr, @RequestParam("poolSize") int poolSize) {
        PaymentProvider provider = PaymentProvider.valueOf(providerStr.toUpperCase());
        gatewayRegistry.resizeWorkerPool(provider, poolSize);
        return ResponseEntity.ok("Worker pool for " + provider + " scaled to " + poolSize + " workers.");
    }
}
