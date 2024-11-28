package com.example.demo.repository;

import com.example.demo.model.DamageReport;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DamageReportRepository {
    private final JdbcTemplate jdbcTemplate;

    public DamageReportRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveDamageReport(DamageReport report) {
        String sql = "INSERT INTO DamageReport (UserID, BikeID, Description, Status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, report.getUserId(), report.getBikeId(), report.getDescription(), report.getStatus());
    }

    public List<DamageReport> findAllReports() {
        String sql = "SELECT * FROM DamageReport";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DamageReport.class));
    }

    public void updateReportStatus(int reportId, String status) {
        String sql = "UPDATE DamageReport SET Status = ? WHERE ReportID = ?";
        jdbcTemplate.update(sql, status, reportId);
    }

    public List<DamageReport> findReportsByStatus(String status) {
        String sql = "SELECT * FROM DamageReport WHERE Status = ?";
        return jdbcTemplate.query(sql, new Object[]{status}, new BeanPropertyRowMapper<>(DamageReport.class));
    }
}
