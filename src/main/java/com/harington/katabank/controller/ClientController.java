package com.harington.katabank.controller;

import java.util.Collection;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.harington.katabank.model.Client;
import com.harington.katabank.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@PostMapping("/batch/start")
	public ResponseEntity<String> startBatchJob(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please provide a file for batch execution.");
		}

		try {
			// Save the file to a temporary location (you might want to save it permanently)
//			Path tempFile = Files.createTempFile("uploadedFile", ".csv");
//			file.transferTo(tempFile);
			
			// Pass the file path as a job parameter
			JobParameters jobParameters = new JobParametersBuilder().addJobParameter("input.file.byte", file.getBytes(), byte[].class)
					.toJobParameters();

			jobLauncher.run(job, jobParameters);

			return ResponseEntity.ok("Batch job started successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error starting batch job.");
		}
	}

	@GetMapping("/find")
	public Collection<Client> findClients(@RequestParam(name = "fullName", required = false) String fullName,
			@RequestParam(name = "email", required = false) String email) {
		return clientService.searchForClient(fullName, email);
	}
	
	@PostMapping("/save")
	public void save(@RequestBody Client client) {
		clientService.addClient(client);
	}

}
