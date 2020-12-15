package com.example.finalbackend.controllers;

import com.example.finalbackend.models.Global;
import com.example.finalbackend.models.Upload;
import com.example.finalbackend.models.User;
import com.example.finalbackend.models.openCpuFileUploadOperation;
import com.example.finalbackend.payloads.response.MessageResponse;
import com.example.finalbackend.repositories.BatchRepository;
import com.example.finalbackend.repositories.GlobalsRepository;
import com.example.finalbackend.repositories.UploadRepository;
import com.example.finalbackend.repositories.UserRepository;
import com.example.finalbackend.services.StorageService;
import com.example.finalbackend.services.storage.StorageFileNotFoundException;
import com.example.finalbackend.worker.ProcessData;
import com.example.finalbackend.worker.WorkerConfig;
import com.example.finalbackend.worker.ocpuExecutor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.context.ApplicationContext;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Based on tutorial from Tamaro Skaljic Username: tamaro-skaljic on GitHub.
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/file")
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UploadRepository uploadRepository;

    @Autowired
    GlobalsRepository globalsRepository;

    @Autowired
    ocpuExecutor executor;

    @Autowired
    WorkerConfig config;

    @ModelAttribute
    public openCpuFileUploadOperation newOp() {
        return new openCpuFileUploadOperation();
    }

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('USER')")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        String path = storageService.store(file);
        /*
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        */
        return path;
    }

    @PostMapping("/{uid}/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> uploadFile(@PathVariable("uid") String userId, @RequestParam("file") MultipartFile file,
                                        RedirectAttributes redirectAttributes) {

        String path = storageService.store(file);
        Optional<User> foundUser = userRepository.findById(Long.parseLong(userId));
        Optional<Global> global = globalsRepository.getFirstById(Long.parseLong("1"));
        if (foundUser.isPresent() && global.isPresent()) {
            User u = foundUser.get();
            Global g = global.get();
            if (u.getBatchCount() == null) {
                u.setBatchCount(1);
            } else {
                u.setBatchCount(u.getBatchCount() + 1);
            }
            Upload up = new Upload();
            up.setName(u.getUsername() + "_" + u.getBatchCount().toString());
            up.setSample(path);
            long millis = new java.util.Date().getTime();
            up.setCreated(millis);
            up.setContributing(false);
            up.setUser(u);
            u.addUpload(up);
            userRepository.save(u);

            if (g.getTotalUploads() == 0) {
                g.setTotalUploads(Long.parseLong("1"));
            } else {
                g.setTotalUploads(g.getTotalUploads() + 1);
            }
            globalsRepository.save(g);

            openCpuFileUploadOperation op = newOp();
            op.setupLoadName(up.getName());
            String funcName = "get_horvath_age_from_csv";
            op.setFunctionName(funcName);
            op.setInputFileLocation(path);
            op.setUserName(u.getUsername());

            //delegate waiting for R to complete - Single Thread as server take ~10 mins to process data
            ProcessData proc = config.newProcessData(storageService, userRepository, uploadRepository, globalsRepository);
            proc.setOp(op);
            executor.execute(proc);

            return ResponseEntity.ok(foundUser.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(new MessageResponse("This user couldn't be found"));
        }
    }

    @GetMapping("/{fid}/download")
    @PreAuthorize("hasRole('USER')")
    public void getFile(@PathVariable("fid") String fileId,
                        HttpServletResponse response) {
        try {
            Path file = storageService.load(fileId);
            //InputStream stream = new FileInputStream(file);
            response.setContentType("application/CSV");
            response.addHeader("Content-Disposition", "attachment; filename="+fileId);
            Files.copy(file, response.getOutputStream());
            response.getOutputStream().flush();

            Optional<Global> global = globalsRepository.getFirstById(Long.parseLong("1"));
            if (global.isPresent()) {
                Global g = global.get();
                if (g.getTotalDownLoaded() == null) {
                    g.setTotalDownLoaded(Long.parseLong("1"));
                } else {
                    g.setTotalDownLoaded(g.getTotalDownLoaded() + 1);
                }
                globalsRepository.save(g);
            }
        } catch (IOException e) {
            System.out.print("couldn't write to IO");
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
