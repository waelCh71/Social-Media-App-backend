package com.example.mediaServices.service;

import com.example.jwtSecurity.models.UserProfile;
import com.example.mediaServices.Repository.MediaRepository;
import com.example.mediaServices.Repository.UserRepository;
import com.example.mediaServices.config.JwtService;
import com.example.mediaServices.models.Media;
import com.example.mediaServices.models.TypeMedia;
import com.example.mediaServices.models.UserMedia;
import com.example.mediaServices.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
@Slf4j
@Service
@RequiredArgsConstructor
public class MedServices {

    @Autowired
    private MediaRepository mediaRepository;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String uploadProfilePicture(MultipartFile file,String token) throws IOException {

        AtomicReference<String> response= new AtomicReference<>("Something went wrong");
        System.out.println("ss: "+file.getContentType());
        if(file.isEmpty()){
            return "Please Select a file";
        }
        if (!file.getContentType().startsWith("image")) {
            return "Only image files are allowed";
        }
        System.out.println(file.getContentType());
        if (file.getSize()>25*1024*1024){
            return "The file exceeds the maximum size limit of 25MB.";
        }
        UserProfile user=findProfileByToken(token);
        Media media = Media.builder().
                name(file.getOriginalFilename()).
                type(TypeMedia.IMAGE).
                data(Utils.compressImage(file.getBytes())).
                build();
        UserMedia userMedia=new UserMedia();
        mediaRepository.findByUserId(user.getId()).ifPresentOrElse(media1 -> {

            media1.setProfile_pic(media);
            mediaRepository.save(media1);
            response.set("Image Saved");
            return;
        },
                ()->{
            userMedia.setUserId(user.getId());
            userMedia.setProfile_pic(media);
            mediaRepository.save(userMedia);
            response.set("Image Saved");
                });

        return String.valueOf(response);
    }

    public String uploadCoverPicture(MultipartFile file,String token) throws IOException {

        AtomicReference<String> response= new AtomicReference<>("Something went wrong");
        if(file.isEmpty()){
            return "Please Select a file";
        }
        if (!file.getContentType().startsWith("image")) {
            return "Only image files are allowed";
        }
        System.out.println(file.getContentType());
        if (file.getSize()>25*1024*1024){
            return "The file exceeds the maximum size limit of 25MB.";
        }
        UserProfile user=findProfileByToken(token);
        Media media = Media.builder().
                name(file.getOriginalFilename()).
                type(TypeMedia.IMAGE).
                data(Utils.compressImage(file.getBytes())).
                build();
        UserMedia userMedia=new UserMedia();
        mediaRepository.findByUserId(user.getId()).ifPresentOrElse(media1 -> {

                    media1.setCover_pic(media);
                    mediaRepository.save(media1);
                    response.set("Image Saved");
                    return;
                },
                ()->{
                    userMedia.setUserId(user.getId());
                    userMedia.setCover_pic(media);
                    mediaRepository.save(userMedia);
                    response.set("Image Saved");
                });

        return String.valueOf(response);
    }

    public byte[] getProfileImages(String token){
        UserProfile user=findProfileByToken(token);
        AtomicReference<UserMedia> userMedia= new AtomicReference<>(new UserMedia());
        mediaRepository.findByUserId(user.getId()).ifPresent(userMedia1-> {
             userMedia.set(userMedia1);
        });
        if (userMedia.getOpaque().getProfile_pic()==null){
            return null;
        }
        var image=userMedia.getOpaque().getProfile_pic().getData();
      //  return Utils.decompressImage(image);
        log.info("image::"+ image.toString()+ "::::"+Utils.decompressImage(image));
        return Utils.decompressImage(image);
    }

    public byte[] getCoverImages(String token){
        UserProfile user=findProfileByToken(token);
        AtomicBoolean verif= new AtomicBoolean(false);
        AtomicReference<UserMedia> userMedia= new AtomicReference<>(new UserMedia());
        mediaRepository.findByUserId(user.getId()).ifPresent(userMedia1-> {
            userMedia.set(userMedia1);
        });
        if (userMedia.getOpaque().getCover_pic()==null){
            return null;
        }
        var image=userMedia.getOpaque().getCover_pic().getData();
        return Utils.decompressImage(image);
    }

    public String deleteProfilePic(String token){
        AtomicReference<String> response= new AtomicReference<>("Something went wrong");

        UserProfile user=findProfileByToken(token);
        mediaRepository.findByUserId(user.getId()).ifPresent(media1 -> {

                    media1.setProfile_pic(null);
                    mediaRepository.save(media1);
                    response.set("Image Deleted");
                });
        return String.valueOf(response);
    }

    public String deleteCoverPic(String token){
        AtomicReference<String> response= new AtomicReference<>("Something went wrong");

        UserProfile user=findProfileByToken(token);
        mediaRepository.findByUserId(user.getId()).ifPresent(media1 -> {

            media1.setCover_pic(null);
            mediaRepository.save(media1);
            response.set("Image Deleted");
        });
        return String.valueOf(response);
    }

    public UserProfile findProfileByToken(String token){
        var jwtToken=token.substring(7);
        var email=jwtService.extractUserName(jwtToken);
        return userRepository.findByEmail(email).orElseThrow();
    }

    public UserProfile findProfileById(String id){
        return userRepository.findById(id).orElseThrow();
    }


}
