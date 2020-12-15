package com.example.finalbackend.models;

import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="shared")
public class UploadShared {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String annotation;

    private Long created;

    private Long completed;

    private Boolean contributing;

    private String sample;

    private String result;

    public UploadShared() {}

    public UploadShared(Upload u) {

        if (u.getName() != null) {
            this.name = u.getName();
        }
        if (u.getAnnotation() != null) {
            this.annotation = u.getAnnotation();
        }
        if (u.getCreated() != null) {
            this.created = u.getCreated();
        }
        if (u.getCompleted() != null) {
            this.completed = u.getCompleted();
        }
        this.contributing = true;
        if (u.getSample() != null) {
            this.sample = u.getSample();
        }
        if (u.getResult() != null) {
            this.result = u.getResult();
        }
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public Boolean getContributing() {
        return contributing;
    }

    public void setContributing(Boolean contributing) {
        this.contributing = contributing;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
