package org.egh.demo.controller;

import org.egh.demo.entity.KPI;
import org.egh.demo.service.KPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kpis")
public class KPIController {

    @Autowired
    private KPIService kpiService;

    @PostMapping
    public ResponseEntity<KPI> createKPI(@RequestBody KPI kpi) {
        return ResponseEntity.ok(kpiService.save(kpi));
    }

    @GetMapping
    public ResponseEntity<List<KPI>> getAllKPIs() {
        return ResponseEntity.ok(kpiService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KPI> getKPIById(@PathVariable Long id) {
        return ResponseEntity.ok(kpiService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KPI> updateKPI(@PathVariable Long id, @RequestBody KPI kpi) {
        return ResponseEntity.ok(kpiService.update(id, kpi));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKPI(@PathVariable Long id) {
        kpiService.delete(id);
        return ResponseEntity.ok().build();
    }
} 