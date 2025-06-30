package org.egh.demo.service;

import org.egh.demo.entity.KPI;
import java.util.List;

public interface KPIService {
    KPI save(KPI kpi);
    List<KPI> findAll();
    KPI findById(Long id);
    KPI update(Long id, KPI kpi);
    void delete(Long id);
} 