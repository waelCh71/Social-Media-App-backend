package com.example.mediaServices.controllers;

import com.example.mediaServices.service.MedServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api5/v1/media-services")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
//@ComponentScan("com.example.mediaServices.Services")
public class MediaController {


    //private MediaTest mediaServices;

    @Autowired
    private MedServices medServices;

    @PostMapping("/uploadProfilePic")
    public @ResponseBody ResponseEntity uploadProfilePic(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(medServices.uploadProfilePicture(file, token));
    }

    @PostMapping("/uploadCoverPic")
    public @ResponseBody ResponseEntity uploadCoverPic(@RequestParam("file") MultipartFile file,@RequestHeader("Authorization") String token) throws IOException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(medServices.uploadCoverPicture(file, token));
    }

    @GetMapping("/getProfilePic")
    public @ResponseBody ResponseEntity getProfilePic(@RequestHeader("Authorization") String token){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(medServices.getProfileImages(token));
    }

    @GetMapping("/getCoverPic")
    public @ResponseBody ResponseEntity getCoverPic(@RequestHeader("Authorization") String token){
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(medServices.getCoverImages(token));
    }

    @PostMapping("/delProfilePic")
    public String delProfilePic(@RequestHeader("Authorization") String token){
        return medServices.deleteProfilePic(token);
    }

    @PostMapping("/delCoverPic")
    public String delCoverPic(@RequestHeader("Authorization") String token){
        return medServices.deleteCoverPic(token);
    }

    /*@GetMapping("/getProfilePic")
    public @ResponseBody ResponseEntity<Resource> getProfilePic(@RequestHeader("Authorization") String token) throws IOException {
        byte[] imageData = medServices.getProfileImages(token);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        BufferedImage bImage = ImageIO.read(bis);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        byte[] newImageData = bos.toByteArray();
        Resource resource = new ByteArrayResource(newImageData);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"profilePic.jpg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }*/


    /*@PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("11");
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();

        String mediaLink = mediaServices.uploadFile(bytes);
        System.out.println("11");
        return ResponseEntity.ok(mediaLink);
    }
    @GetMapping
    public String demoController(){
        return mediaServices.print();
        //return "Hello from secured end point";
    }*/
}
