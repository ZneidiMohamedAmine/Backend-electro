package org.egh.demo.service.impl;

import org.egh.demo.entity.KPI;
import org.egh.demo.repository.KPIRepository;
import org.egh.demo.service.KPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KPIServiceImpl implements KPIService {

    @Autowired
    private KPIRepository kpiRepository;

    @Override
    public KPI save(KPI kpi) {
        return kpiRepository.save(kpi);
    }

    @Override
    public List<KPI> findAll() {
        return kpiRepository.findAll();
    }

    @Override
    public KPI findById(Long id) {
        return kpiRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("KPI not found with id: " + id));
    }

    @Override
    public KPI update(Long id, KPI kpi) {
        KPI existingKPI = findById(id);
        existingKPI.setChiffreAffaire(kpi.getChiffreAffaire());
        existingKPI.setCoutTotal(kpi.getCoutTotal());
        existingKPI.setMarge(kpi.getMarge());
        existingKPI.setTempsRealisation(kpi.getTempsRealisation());
        existingKPI.setProjet(kpi.getProjet());
        return kpiRepository.save(existingKPI);
    }

    @Override
    public void delete(Long id) {
        kpiRepository.deleteById(id);
    }
} 