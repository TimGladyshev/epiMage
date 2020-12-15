package com.example.finalbackend.models;

import javax.persistence.*;

@Entity
@Table (name = "global")
public class Global {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalUploads;

    private Long totalShared;

    private Long totalDownLoaded;

    private Long totalSuccess;

    public Long getTotalUploads() {
        return totalUploads;
    }

    public void setTotalUploads(Long totalUploads) {
        this.totalUploads = totalUploads;
    }

    public Long getTotalShared() {
        return totalShared;
    }

    public void setTotalShared(Long totalShared) {
        this.totalShared = totalShared;
    }

    public Long getTotalDownLoaded() {
        return totalDownLoaded;
    }

    public void setTotalDownLoaded(Long totalDownLoaded) {
        this.totalDownLoaded = totalDownLoaded;
    }

    public Long getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(Long totalSuccess) {
        this.totalSuccess = totalSuccess;
    }
}
