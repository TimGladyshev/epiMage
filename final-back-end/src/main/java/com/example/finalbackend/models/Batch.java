package com.example.finalbackend.models;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "batches")
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String batchID;

    private String annotation;

    private Long created;

    private Long completed;

    private Boolean contributing;

    @OneToMany(
            mappedBy = "batch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Sample> samples = new ArrayList<>();

    @OneToMany(
            mappedBy = "batch",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Result> results = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ElementCollection
    private List<String> sampleNames = new ArrayList<>();

    public Batch() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public Boolean getContributing() {
        return contributing;
    }

    public void setContributing(Boolean contributing) {
        this.contributing = contributing;
    }

    public List<Sample> getSamples() {
        return samples;
    }

    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public Batch addSample(Sample sample) {
        samples.add(sample);
        sampleNames.add(sample.getSampleID());
        sample.setBatch(this);
        return this;
    }

    public Batch removeSample(Sample sample) {
        samples.remove(sample);
        sampleNames.remove(sample.getSampleID());
        sample.setBatch(null);
        return this;
    }

    public Batch addResult(Result result) {
        results.add(result);
        result.setBatch(this);
        return this;
    }

    public Batch removeResult(Result result) {
        results.remove(result);
        result.setBatch(null);
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getSampleNames() {
        return sampleNames;
    }

    public void setSampleNames(List<String> sampleNames) {
        this.sampleNames = sampleNames;
    }

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }
}
