package com.collier.jc.SignatureConverter.controllers;

import com.collier.jc.SignatureConverter.util.ImageHandler;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author Jordan
 *
 * Controller class exposing REST endpoints which can accept image files for further processing.
 *
 * */
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
    /*
    * Method exposes endpoint allowing user to post a JPEG file to an endpoint and recieve a file as the response.
    * #TODO Name file in response
    * #TODO check shape of image (is it rectangle? if not crop out the signature)
    * */
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
