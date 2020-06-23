package se.simpleblog.blog.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import se.simpleblog.blog.domain.ProfileImage;
import se.simpleblog.blog.repository.ProfileImageRepository;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Service
public class ProfileImageService {

    private final ProfileImageRepository repository;

    @Autowired
    public ProfileImageService(ProfileImageRepository repository) {
        this.repository = repository;
    }

    @SneakyThrows
    public ProfileImage save(MultipartFile file) {
        String fileName = StringUtils.cleanPath(requireNonNull(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new Exception("Invalid file");
            }

            ProfileImage image = new ProfileImage(fileName, file.getContentType(), file.getBytes());
            return repository.save(image);
        } catch (Exception exception) {
            throw new Exception("Could not persist");
        }

    }
}
