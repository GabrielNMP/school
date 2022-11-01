package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
@Transactional
public class AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }


    public void uploadAvatar(Long id, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.getReferenceById(id);

        Path filePath = Path.of(avatarsDir, student + "."
                + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
                ) {
            bis.transferTo(bos);
        }
        Avatar avatar =avatarRepository.findByStudentId(id).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        logger.info(" Method -uploadAvatar- started ");
        avatarRepository.save(avatar);
    }

    private byte[] generateDataForDB (Path filePath) throws IOException {
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);


            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    public Avatar findAvatar(Long id) {
        logger.info("Method -findAvatar- started ");
        return avatarRepository.findByStudentId(id).orElse(new Avatar());
    }

    private String getExtensions(String fileName) {
        logger.info("Method -getExtensions- started ");
        return fileName.substring(fileName.lastIndexOf(".") + 1);

    }

    public List<Avatar> allAvatarsWithPagination(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber-1, pageSize);
        logger.info("Method -allAvatarsWithPagination- started ");
        return avatarRepository.findAll(pageRequest).getContent();
    }

}
