package se.simpleblog.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import se.simpleblog.blog.domain.ProfileImage;
import se.simpleblog.blog.service.ProfileImageService;

@RestController
@RequestMapping("/upload")
public class ProfileImageController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileImageController.class);
    private final ProfileImageService profileImageService;

    @Autowired
    public ProfileImageController(ProfileImageService profileImageService) {
        this.profileImageService = profileImageService;
    }

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        ProfileImage profileImage = profileImageService.save(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(profileImage.getId().toString())
                .toUriString();

        return ResponseEntity.ok("Successfully uploaded image");
    }

}
