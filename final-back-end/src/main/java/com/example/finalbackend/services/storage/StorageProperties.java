package com.example.finalbackend.services.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
    String userDirectory = Paths.get("")
            .toAbsolutePath()
            .toString();
    File dir = new File(userDirectory + "/epiStore");
    boolean successful = dir.mkdir();

    private String location = userDirectory + "/epiStore";
    */
    private String location = "/home/tomcat/fileStorage/";
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
