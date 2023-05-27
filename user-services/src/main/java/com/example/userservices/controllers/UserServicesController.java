package com.example.userservices.controllers;


import com.example.jwtSecurity.models.UserProfile;
import com.example.userservices.Services.UserService;
import com.example.userservices.models.UrlMedia;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/v2/user-services")
@RequiredArgsConstructor
public class UserServicesController {

    @Autowired
    private UserService userService;

    @PostMapping("/get-user")
    public ResponseEntity<UserProfile> getUserByToken (@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(userService.findProfileByToken(token));
    }

    @GetMapping("/getUser-ById")
    public @ResponseBody ResponseEntity<UserProfile> findUserById(@RequestParam String id){
        return userService.findProfileById(id);
    }

    @PostMapping("/complete-info")
    public @ResponseBody UserProfile completeAllUserInfo(@RequestBody UserProfile request,
                                                  @RequestHeader("Authorization") String token){
         return userService.completeAllUserInfo(request,token);
    }

    @PostMapping("/update-name")
    public @ResponseBody ResponseEntity<?> updateUserName(@RequestParam String newFirstName,
                                                    @RequestParam String newLastName,
                                                    @RequestParam String password,
                                                    @RequestHeader("Authorization") String token) {

        if(userService.updateUserName(newFirstName,newLastName,password,token)!=null){
            return ResponseEntity.ok().body(userService.updateUserName(newFirstName,newLastName,password,token));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/update-profilePic")
    public @ResponseBody ResponseEntity<UserProfile> updateProfilePic(@RequestBody UrlMedia url,
                                                          @RequestHeader("Authorization") String token){

        System.out.println("test profile");
            return ResponseEntity.ok().body(userService.updateProfilePic(url.getUrl(), token));
    }

    @PostMapping("/update-coverPic")
    public @ResponseBody ResponseEntity<UserProfile> updateCoverPic(@RequestBody UrlMedia url,
                                                         @RequestHeader("Authorization") String token){
        System.out.println("test cover");
        return ResponseEntity.ok().body(userService.updateCoverPic(url.getUrl(), token));
    }

    @PostMapping("/update-phone")
    public @ResponseBody ResponseEntity updatePhoneNumber(@RequestParam String phone,
                                                       @RequestHeader("Authorization") String token){
        if(userService.updateUserPhoneNumber(phone,token)!=null){
            return ResponseEntity.ok().body(userService.updateUserPhoneNumber(phone,token));
        }
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/update-info")
    public @ResponseBody ResponseEntity<UserProfile> updateUserInfo(@RequestParam (required = false) String newCountry,
                                                                    @RequestParam (required = false) String newCity,
                                                                    @RequestParam (required = false) String newDOB,
                                                                    @RequestParam (required = false) String newBio,
                                                                    @RequestHeader("Authorization") String token){
        System.out.println(newCountry);
        return ResponseEntity.ok()
                .body(userService.updateUserInfo(newCountry,newCity,newDOB,newBio,token));
    }

    @PostMapping("/getAllUsers")
    public  ResponseEntity<List<UserProfile>> getAllUsers(){
        return userService.getAllProfileUser();
    }


    @GetMapping("/findUserByName")
    public ResponseEntity<List<UserProfile>> findUserByName(@RequestParam String name){
        return ResponseEntity.ok().body(userService.findUserByName(name));
    }

    @PostMapping("/findUserByCity")
    public ResponseEntity<List<UserProfile>> findUserByCity(@RequestParam String city){
        return ResponseEntity.ok().body(userService.findUserByCity(city));
    }

    @PostMapping("/findUserByPhone")
    public  ResponseEntity<List<UserProfile>> findUserByPhone(@RequestParam long phone){
        return ResponseEntity.ok().body(userService.findUserByPhone(phone));
    }

    @PostMapping("/filterByNameAndCity")
    public ResponseEntity<List<UserProfile>> filterByNameAndCity(@RequestParam String name,
                                                                 @RequestParam (required=false) String city){
        return ResponseEntity.ok().body(userService.filterUserByNameAndCity(name,city));
    }

    @PostMapping("/delete-profile")
    public  ResponseEntity<String> deleteProfile(@RequestParam String password,
                                                              @RequestHeader("Authorization") String token){
        return userService.deleteProfile(password,token);
    }

    @PostMapping("/setUserStatut")
    public ResponseEntity<String> setUserStatut(@RequestHeader("Authorization") String token){
        return userService.setUserStatut(token);
    }

    @GetMapping
    public  @ResponseBody String test(){
        return "Hello , Unsecured END POINT";
    }
}
