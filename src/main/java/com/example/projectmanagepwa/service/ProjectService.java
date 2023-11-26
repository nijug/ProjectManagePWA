package com.example.projectmanagepwa.service;

import com.example.projectmanagepwa.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {

    @Value("${service.api.url}")
    private String apiUrl;

    @Value("${service.api.create-endpoint}")
    private String createEndpoint;

    @Value("${service.api.read-all-endpoint}")
    private String readAllEndpoint;

    private final RestTemplate restTemplate;
    @Autowired
    public ProjectService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addProjects(Project project) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Project> requestEntity = new HttpEntity<>(project, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl + createEndpoint, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
            System.out.println("Project created successfully.");
        } else {
            System.out.println("Failed to create the project. HTTP Status Code: " + responseEntity.getStatusCodeValue());
            System.out.println(responseEntity.getBody());
        }
    }

    public Project[] readProjects() {
        ResponseEntity<Project[]> responseEntity = restTemplate.getForEntity(apiUrl + readAllEndpoint, Project[].class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            System.err.println("Error: HTTP status code " + responseEntity.getStatusCodeValue());
        }

        return null;
    }

    public void deleteProjects(Integer projectId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/" + projectId,
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Project deleted successfully.");
        } else {
            System.err.println("Failed to delete the project. HTTP Status Code: " + responseEntity.getStatusCodeValue());
        }
    }


    public void updateProjects(Project updatedProject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Project> requestEntity = new HttpEntity<>(updatedProject, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiUrl + "/" + updatedProject.getId(),
                HttpMethod.PUT,
                requestEntity,
                String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.println("Project updated successfully.");
        } else {
            System.out.println("Failed to update the project. HTTP Status Code: " + responseEntity.getStatusCodeValue());
            System.out.println(responseEntity.getBody());
        }
    }

    public Project getProjectById(Integer projectId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Project> responseEntity = restTemplate.exchange(
                apiUrl + "/" + projectId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Project.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            System.err.println("Error: HTTP status code " + responseEntity.getStatusCodeValue());
        }

        return null;
    }

}
