package com.ilyes.ClientsRegions.controllers;

import com.ilyes.ClientsRegions.entities.Client;
import com.ilyes.ClientsRegions.entities.User;
import com.ilyes.ClientsRegions.repositories.ClientRepository;
import com.ilyes.ClientsRegions.repositories.UserRepository;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class ImageUploadController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping(value = "upload/client", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> handleFileUploadFormClient(
            @RequestPart("file") MultipartFile file,
            @RequestParam("clientID") String clientID
    ) throws IOException {
        try {
            if(!file.isEmpty()) {
                Long client_id = Long.parseLong(clientID);
                //log.info("Received file: {}", file.getOriginalFilename());

                //File f = new ClassPathResource("").getFile();
                //final Path path = Paths.get(f.getAbsolutePath() + File.separator + "static" + File.separator + "image");
                final Path path = Paths.get("uploads");
                String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = new Date().getTime() + extension;

                List<Client> clients = clientRepository.findAll();
                for (Client client: clients) {
                    if(client.getImagePath() == null || Objects.equals(client.getIdClient(), client_id)) {
                        client.setImagePath(fileName);
                        clientRepository.save(client);
                    }
                }

                // Upload image:
                Path filePath = path.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Return image info:
                String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/" + fileName)
                        .toUriString();

                var result = Map.of(
                        "filename", fileName,
                        "fileUri", fileUri
                );
                return ok().body(result);
            }   else {
                return ok().body(null);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "upload/user", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> handleFileUploadFormUser(
            @RequestPart("file") MultipartFile file,
            @RequestPart("userEmail") String userEmail
    ) throws IOException {
        try {
            //log.info("Received file: {}", file.getOriginalFilename());

            //File f = new ClassPathResource("").getFile();
            //final Path path = Paths.get(f.getAbsolutePath() + File.separator + "static" + File.separator + "image");
            final Path path = Paths.get("uploads");
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = new Date().getTime() + extension;

            List<User> users = userRepository.findAll();
            for (User user: users) {
                if(user.getImagePath() == null || userEmail.equals(user.getEmail())) {
                    user.setImagePath(fileName);
                    userRepository.save(user);
                }
            }

            // Upload image:
            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Return image info:
            String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/" + fileName)
                    .toUriString();

            var result = Map.of(
                    "filename", fileName,
                    "fileUri", fileUri
            );
            return ok().body(result);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/image/{filename}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        // Get the file from the uploads directory
        File file = new File("uploads/" + filename);
        Path path = file.toPath();
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + file.getName());

        // Return the image as a ResponseEntity
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                .body(resource);
    }

}