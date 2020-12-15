package com.example.finalbackend.models;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table (name = "results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    private String SampleID;

    private Double DNAmAge;

    private String Comment;

    private Integer noMissingPerSample;

    private Double meanMethBySample;

    private Double minMethBySample;

    private Double maxMethBySample;

    private String predictedGender;

    private Double meanXchromosome;

    @ManyToOne(fetch = FetchType.LAZY)
    private Batch batch;

    public Long getId() {
        return this.id;
    }

    public String getSampleID() {
        return SampleID;
    }

    public void setSampleID(String sampleID) {
        SampleID = sampleID;
    }

    public Double getDNAmAge() {
        return DNAmAge;
    }

    public void setDNAmAge(Double DNAmAge) {
        this.DNAmAge = DNAmAge;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public Integer getNoMissingPerSample() {
        return noMissingPerSample;
    }

    public void setNoMissingPerSample(Integer noMissingPerSample) {
        this.noMissingPerSample = noMissingPerSample;
    }

    public Double getMeanMethBySample() {
        return meanMethBySample;
    }

    public void setMeanMethBySample(Double meanMethBySample) {
        this.meanMethBySample = meanMethBySample;
    }

    public Double getMinMethBySample() {
        return minMethBySample;
    }

    public void setMinMethBySample(Double minMethBySample) {
        this.minMethBySample = minMethBySample;
    }

    public Double getMaxMethBySample() {
        return maxMethBySample;
    }

    public void setMaxMethBySample(Double maxMethBySample) {
        this.maxMethBySample = maxMethBySample;
    }

    public String getPredictedGender() {
        return predictedGender;
    }

    public void setPredictedGender(String predictedGender) {
        this.predictedGender = predictedGender;
    }

    public Double getMeanXchromosome() {
        return meanXchromosome;
    }

    public void setMeanXchromosome(Double meanXchromosome) {
        this.meanXchromosome = meanXchromosome;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result)) return false;
        return id != null && id.equals(((Result) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
