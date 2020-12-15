package com.example.finalbackend.models;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table( name = "samples")
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String SampleID;

    @OneToMany(
            mappedBy = "sample",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Site> sites;

    @ManyToOne(fetch = FetchType.LAZY)
    private Batch batch;

    public String getSampleID() {
        return SampleID;
    }

    public void setSampleID(String sampleID) {
        this.SampleID = sampleID;
    }

    public Long getId() {
        return this.id;
    }

    public List<Site> getSites() {
        return sites;
    }

    public Sample addSite(Site site) {
        sites.add(site);
        site.setSample(this);
        return this;
    }

    public Sample removeSite(Site site) {
        sites.remove(site);
        site.setSample(null);
        return this;
    }

    public void setSites(List<Site> sites) {
        this.sites = sites;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sample)) return false;
        return id != null && id.equals(((Sample) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
