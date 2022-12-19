package com.backgroundRemover.Background;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@org.springframework.stereotype.Controller
public class Controller{
    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        System.out.println("**********************************************");
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getName());
        System.out.println(file.getContentType());
        System.out.println(file.getSize());

        // Create Image instance
        Image img = new Image();
        img.setImageName(file.getOriginalFilename());
        imageRepository.save(img);

        // Store image in static target folder
        String DirectoryPath = new ClassPathResource("static/images/").getFile().getAbsolutePath();
        Path imgPath = Paths.get(DirectoryPath + File.separator + file.getOriginalFilename());

        Files.copy(file.getInputStream() , imgPath, StandardCopyOption.REPLACE_EXISTING);

        // Success message
        session.setAttribute("msg", "Image uploaded successfully");

        return "redirect:/";
    }
}
