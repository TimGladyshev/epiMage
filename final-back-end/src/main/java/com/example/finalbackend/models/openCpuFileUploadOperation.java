package com.example.finalbackend.models;

import java.io.Serializable;

public class openCpuFileUploadOperation implements Serializable {
    private String inputFileLocation;

    private String functionName;

    private String userName;

    private String upLoadName;


    public String getInputFileLocation() {
        return inputFileLocation;
    }

    public void setInputFileLocation(String inputFileLocation) {
        this.inputFileLocation = inputFileLocation;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getupLoadName() {
        return upLoadName;
    }

    public void setupLoadName(String batchName) {
        this.upLoadName = batchName;
    }
}
