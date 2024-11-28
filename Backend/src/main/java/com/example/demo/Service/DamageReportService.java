package com.example.demo.Service;

import com.example.demo.model.DamageReport;
import com.example.demo.repository.DamageReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageReportService {
    @Autowired
    private DamageReportRepository damageReportRepository;

    public void createReport(DamageReport report) {
        damageReportRepository.saveDamageReport(report);
    }

    public List<DamageReport> getAllReports() {
        return damageReportRepository.findAllReports();
    }

    public void verifyReport(int reportId) {
        damageReportRepository.updateReportStatus(reportId, "Verified");
    }

    public List<DamageReport> getReportsByStatus(String status) {
        return damageReportRepository.findReportsByStatus(status);
    }
}
