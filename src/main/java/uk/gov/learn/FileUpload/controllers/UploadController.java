package uk.gov.learn.FileUpload.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.learn.FileUpload.util.ImageHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@RestController
public class UploadController {

    @PostMapping("/")
    public String handleUpload(@RequestParam("file") MultipartFile file) {

        String size, type, name;
        size = Long.toString(file.getSize());
        type = file.getContentType();
        name = file.getName();

        return "file accepted! name:  " + name + ", size: " + size + ", type:  " + type;
    }

    @PostMapping(value = "/convert", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody ResponseEntity<byte[]> handleImageUpload(@RequestParam("file") MultipartFile file) {
        try {
            ImageHandler ih = new ImageHandler(file.getBytes());
            File out = ih.imageConverter(ih.getBi(), "image/jpeg");
            InputStream is = new FileInputStream(out);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(IOUtils.toByteArray(is));
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
