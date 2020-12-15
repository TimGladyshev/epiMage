package com.example.finalbackend.worker;

import com.example.finalbackend.models.*;
import com.example.finalbackend.repositories.BatchRepository;
import com.example.finalbackend.repositories.GlobalsRepository;
import com.example.finalbackend.repositories.UploadRepository;
import com.example.finalbackend.repositories.UserRepository;
import com.example.finalbackend.services.StorageService;
import okhttp3.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.nio.file.Path;

//@Service
//@Configurable
//@Scope("prototype")
public class ProcessData implements Runnable {

    private final StorageService storageService;

    //@Autowired(required = true)
    UserRepository userRepository;

    //@Autowired(required = true)
    UploadRepository uploadRepository;

    GlobalsRepository globalsRepository;

    private openCpuFileUploadOperation op;


    @Autowired(required = true)
    public ProcessData(StorageService storageService,
                       UserRepository ur,
                       UploadRepository up,
                       GlobalsRepository glob) {
        this.storageService = storageService;
        this.userRepository = ur;
        this.uploadRepository = up;
        this.globalsRepository = glob;
    }

    //@Override
    public void run() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.MINUTES)
                .writeTimeout(60, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .followRedirects(true)
                .build();

        System.out.print(this.op.getUserName());
        Optional<Upload> upload = uploadRepository.findByName(op.getupLoadName());
        Optional<User> user = userRepository.findByUsername(this.op.getUserName());
        if (upload.isPresent() && user.isPresent()) {
            Upload u = upload.get();
            User usr = user.get();
            try {
                File file = storageService.loadAsResource(op.getInputFileLocation()).getFile();
                RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("INP_FILE", file.getName(),
                                RequestBody.create(MediaType.parse("text/csv"), file))
                        .build();
                Request request = new Request.Builder()
                        .url("http://23.227.167.187/ocpu/library/epiClock/R/get_horvath_age_from_csv")
                        .post(requestBody)
                        .build();
                Response res = client.newCall(request).execute();
                if (!res.isSuccessful()) {
                    u.setResult("failed");
                    uploadRepository.save(u);
                    return;
                } else {
                    String rb = res.body().string();
                    System.out.print(rb);
                    String[] rbs = rb.split("\n");
                    System.out.print(rbs[rbs.length - 2]);


                    Request request2 = new Request.Builder()
                            .url("http://23.227.167.187/" + rbs[rbs.length - 2])
                            .build();
                    Response res2 = client.newCall(request2).execute();
                    if (!res2.isSuccessful()) {
                        throw new IOException("Failed to download file: " + res2);
                    }
                    Path path = storageService.load(u.getName()+ "_results.csv");
                    u.setResult(path.getFileName().toString());
                    long millis = new java.util.Date().getTime();
                    u.setCompleted(millis);
                    if (usr.getCompletedCount() == null) {
                        usr.setCompletedCount(1);
                    } else {
                        usr.setCompletedCount(usr.getCompletedCount() + 1);
                    }
                    FileOutputStream fos = new FileOutputStream(String.valueOf(path));
                    fos.write(res2.body().bytes());
                    fos.close();
                    uploadRepository.save(u);

                    Optional<Global> global = globalsRepository.getFirstById(Long.parseLong("1"));
                    if (global.isPresent()) {
                        Global glob = global.get();
                        if (glob.getTotalSuccess() == null) {
                            glob.setTotalSuccess(Long.parseLong("1"));
                        } else {
                            glob.setTotalSuccess(glob.getTotalSuccess() + 1);
                        }
                        globalsRepository.save(glob);
                    }
                }
            } catch (IOException e) {
                System.out.print("cant access DB");
                e.printStackTrace();
            }
        }
    }

    public openCpuFileUploadOperation getOp() {
        return op;
    }

    public void setOp(openCpuFileUploadOperation op) {
        this.op = op;
    }
}
