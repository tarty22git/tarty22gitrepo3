package com.cth.job.api.controller;

import com.cth.job.service.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Reports", description = "Endpoints for online report viewing and CSV export")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/online")
    @Operation(summary = "View report online")
    public ResponseEntity<Map<String, Object>> getOnlineReport(@RequestParam(value = "type", defaultValue = "GATEWAYS") String type) {
        return ResponseEntity.ok(reportService.generateOnlineReport(type));
    }

    @GetMapping("/export/csv")
    @Operation(summary = "Export report to CSV")
    public ResponseEntity<byte[]> exportCsv(@RequestParam(value = "type", defaultValue = "GATEWAYS") String type) {
        byte[] csvData = reportService.exportReportCsv(type);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + type.toLowerCase() + "_report.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csvData);
    }
}
