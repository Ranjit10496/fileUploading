package com.sunglowsys.controllers;

import com.sunglowsys.payload.FileResponse;
import com.sunglowsys.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    @Value("${project.image}")
    private String path;

    @Value("${file.size}")
    private long fileSize;

    private List<String> exts = Arrays.asList(new String[]{"jpg", "jpeg", "png"});

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(@RequestParam("image") MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();
        if (!exts.contains(getExtension(fileName))) {
            return new ResponseEntity<>(new FileResponse(fileName,"Invalid file Extension. Allowed only: " + exts), HttpStatus.BAD_REQUEST);
        }
        if (image.getSize() > fileSize) {
            return new ResponseEntity<>(new FileResponse(image.getOriginalFilename(), "please upload file less then" + (fileSize+1) + "KB size"), HttpStatus.NOT_ACCEPTABLE);
        }

        this.fileService.uploadImage(path, image);


        /*try {
            fileName = this.fileService.uploadImage(path, image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(null,"image is not uploaded due to error on server!!"),HttpStatus.INTERNAL_SERVER_ERROR);

        }*/
        return new ResponseEntity<>(new FileResponse(fileName,"image is successfully upload"), HttpStatus.OK);

    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
