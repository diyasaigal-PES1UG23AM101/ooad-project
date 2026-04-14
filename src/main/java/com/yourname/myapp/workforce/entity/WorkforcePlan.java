package com.yourname.myapp.workforce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "workforce_plan")
public class WorkforcePlan implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "open_positions", nullable = false)
    private int openPositions;

    @Column(name = "hiring_forecast", nullable = false)
    private int hiringForecast;

    @Column(name = "hr_cost_projections", nullable = false)
    private BigDecimal hrCostProjections;

    @Column(name = "quarter", nullable = false)
    private String quarter;

    @Column(name = "total_budget", nullable = false)
    private BigDecimal totalBudget;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getOpenPositions() {
        return openPositions;
    }

    public void setOpenPositions(int openPositions) {
        this.openPositions = openPositions;
    }

    public int getHiringForecast() {
        return hiringForecast;
    }

    public void setHiringForecast(int hiringForecast) {
        this.hiringForecast = hiringForecast;
    }

    public BigDecimal getHrCostProjections() {
        return hrCostProjections;
    }

    public void setHrCostProjections(BigDecimal hrCostProjections) {
        this.hrCostProjections = hrCostProjections;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public BigDecimal getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    @Override
    public WorkforcePlan clone() {
        try {
            WorkforcePlan cloned = (WorkforcePlan) super.clone();
            cloned.setId(null);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("Failed to clone WorkforcePlan", e);
        }
    }
}
