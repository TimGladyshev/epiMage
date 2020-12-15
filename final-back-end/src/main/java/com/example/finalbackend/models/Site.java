package com.example.finalbackend.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "sites")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String ProbeID;

    private Double val;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sample sample;


    public Long getId() {
        return id;
    }

    public String getCpg() {
        return ProbeID;
    }

    public void setCpg(String cpg) {
        this.ProbeID = cpg;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site)) return false;
        return id != null && id.equals(((Site) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
