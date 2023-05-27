package com.example.userFriendzone.controllers;

import com.example.jwtSecurity.models.UserProfile;
import com.example.userFriendzone.Services.UserFriendzoneService;
import com.example.userFriendzone.models.FriendShipStatut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api3/v1/friend-services")
@RequiredArgsConstructor
public class UserFriendzoneController {

    @Autowired
    private final UserFriendzoneService friendzoneService;

    @PostMapping("/sendRequest")
    public ResponseEntity<String> addRequest(@RequestParam String idUserRequest, @RequestHeader("Authorization") String token){
        return friendzoneService.addFriendRequestReceived(idUserRequest, token);
    }

    @PostMapping("/declineRequest")
    public  ResponseEntity<String> deleteRequest(@RequestParam String idUserRequest, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(String.valueOf(friendzoneService.declineFriendRequest(idUserRequest, token)));
    }

    @PostMapping("/cancelRequest")
    public  ResponseEntity<String> cancelRequest(@RequestParam String idUserRequest, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(String.valueOf(friendzoneService.cancelRequestSent(idUserRequest, token)));
    }

    @PostMapping("/acceptRequest")
    public  ResponseEntity<String> acceptRequest(@RequestParam String idFriend, @RequestHeader("Authorization") String token){
        System.out.println("accept work");
        return friendzoneService.acceptRequest(idFriend,token);
    }

    /*@PostMapping("/addFriend")
    public @ResponseBody String addFriend(@RequestParam String idFriend, @RequestHeader("Authorization") String token){
        return String.valueOf(friendzoneService.addUserToFriendList(idFriend,token));
    }*/

    @PostMapping("/deleteFriend")
    public  ResponseEntity<String> deleteFriend(@RequestParam String idFriend, @RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(String.valueOf(friendzoneService.deleteFriend(idFriend,token)));
    }

    @PostMapping("/blockFriend")
    public ResponseEntity<String> blockFriend(@RequestParam String idFriend, @RequestHeader("Authorization") String token){
        return friendzoneService.blockUser(idFriend,token);
    }

    @PostMapping("/unblockFriend")
    public ResponseEntity<String> unblockFriend(@RequestParam String idFriend, @RequestHeader("Authorization") String token){
        return friendzoneService.unblockUser(idFriend,token);
    }


    @PostMapping("/verifyFriendShip")
    public ResponseEntity<FriendShipStatut> verifyFriendShip(@RequestParam String idUser, @RequestHeader("Authorization") String token){
        System.out.println("works");
        return friendzoneService.verifyFriendShip(idUser,token);
    }

    @GetMapping("/friendList")
    public ResponseEntity<List<UserProfile>> getFriendList(@RequestParam String idUser,@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(friendzoneService.getFriendList(idUser,token));
    }

    @GetMapping("/requestList")
    public  ResponseEntity<List<UserProfile>> getRequestList(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(friendzoneService.getRequestList(token));
    }

    @GetMapping("/requestSentList")
    public ResponseEntity<List<UserProfile>> getrequestSentList(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(friendzoneService.getRequestSentList(token));
    }

    @GetMapping("/blockedList")
    public ResponseEntity<List<UserProfile>> getblockedList(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(friendzoneService.getBlockedList(token));
    }

    @GetMapping("/suggestionList")
    public ResponseEntity<List<UserProfile>> getSuggestionList(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(friendzoneService.getSuggestionList(token));
    }

    @GetMapping("/findUser")
    public @ResponseBody UserProfile getUser(String id){
        return friendzoneService.findProfileById(id);
    }
}
