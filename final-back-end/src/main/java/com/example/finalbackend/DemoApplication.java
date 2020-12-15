package com.example.finalbackend;

import com.example.finalbackend.models.Global;
import com.example.finalbackend.models.openCpuFileUploadOperation;
import com.example.finalbackend.repositories.GlobalsRepository;
import com.example.finalbackend.services.StorageService;
import com.example.finalbackend.services.storage.StorageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@EnableSpringConfigured
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(
			StorageService storageService,
			GlobalsRepository globalsRepository) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
			Optional<Global> global = globalsRepository.findById(Long.parseLong("1"));
			if (!global.isPresent()) {
				Global glob = new Global();
				glob.setTotalSuccess(Long.parseLong("0"));
				glob.setTotalUploads(Long.parseLong("0"));
				glob.setTotalShared(Long.parseLong("0"));
				glob.setTotalDownLoaded(Long.parseLong("0"));
				globalsRepository.save(glob);
			}
		};
	}
}
