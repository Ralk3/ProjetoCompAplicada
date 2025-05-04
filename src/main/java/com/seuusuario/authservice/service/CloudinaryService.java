package com.seuusuario.authservice.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    public Map<String, Object> uploadImagem(MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("upload_preset", "servico_facil");

        File convFile = File.createTempFile("upload", file.getOriginalFilename());
        file.transferTo(convFile);
        form.add("file", new FileSystemResource(convFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(form, headers);

        String url = "https://api.cloudinary.com/v1_1/debz34w74/image/upload";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return response.getBody();
    }
}
