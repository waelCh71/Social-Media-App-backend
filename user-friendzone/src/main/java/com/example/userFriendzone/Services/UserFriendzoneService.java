package com.example.userFriendzone.Services;

import com.example.jwtSecurity.models.UserProfile;
import com.example.userFriendzone.config.JwtService;
import com.example.userFriendzone.models.FriendShipStatut;
import com.example.userFriendzone.models.UserFriendzone;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UserFriendzoneService {

    private final UserFriendzoneRepository friendzoneRepository;

    private final JwtService jwtService;
    private final UserRepository userRepository;


    public ResponseEntity<String> addFriendRequestReceived(String idFriendRequestReceived, String token){
        AtomicReference<String> response= new AtomicReference<>("Request Sent");
        if(!userRepository.existsById(idFriendRequestReceived)){

            return ResponseEntity.badRequest().body("User Doesn't Exist");
        }
        String idSender=findProfileByToken(token).getId();

        System.out.println(userRepository.findById(idFriendRequestReceived));
        if (idFriendRequestReceived.equals(idSender)){
            response.set("Wrong request");
            return ResponseEntity.badRequest().body(String.valueOf(response.get()));
        }
        var verif= verifyFriendShip(idFriendRequestReceived,token).getBody();
        System.out.println(verif);
        AtomicReference<FriendShipStatut> statut= new AtomicReference<>(FriendShipStatut.SENDYOUREQUEST);
        AtomicReference<FriendShipStatut> statut1= new AtomicReference<>(FriendShipStatut.WAITINGFORACCEPT);
        AtomicReference<FriendShipStatut> statut2= new AtomicReference<>(FriendShipStatut.FRIENDS);
        AtomicReference<FriendShipStatut> statut3= new AtomicReference<>(FriendShipStatut.BLOCKED);
        AtomicReference<FriendShipStatut> statut4= new AtomicReference<>(FriendShipStatut.BLOCKEDYOU);

        var a=String.valueOf(verif);
        var b=String.valueOf(statut);
        var c=String.valueOf(statut1);
        var d=String.valueOf(statut2);
        var e=String.valueOf(statut3);
        var f=String.valueOf(statut4);
        if(a.equals(b)){
            acceptRequest(idFriendRequestReceived,token);

            return ResponseEntity.ok().body("User Already Send You a  Request\nFriend Added");
        }
        if(a.equals(c)){

            return ResponseEntity.badRequest().body("Request Already Sent");
        }
        if(a.equals(d)){

            return ResponseEntity.badRequest().body("Already Friends");
        }
        if(a.equals(e)){

            return ResponseEntity.badRequest().body("Can't Send Request, Check your Blocked List");
        }

        if(a.equals(f)){

            return ResponseEntity.badRequest().body("Can't Send Request To This User, Try Later");
        }


        friendzoneRepository.findByUserID(idFriendRequestReceived).ifPresentOrElse(userFriendzone -> {
                    var newList=userFriendzone.getFriendRequestList();
                    if(newList!=null){
                        if (newList.contains(idSender)){
                            response.set("Already Sent");
                            return;
                        }
                    }
                    if(newList==null){
                        newList=List.of(idSender);
                    }else {
                        newList.add(idSender);
                    }
                        userFriendzone.setFriendRequestList(newList);
                        friendzoneRepository.save(userFriendzone);
                        addRequestSentList(idFriendRequestReceived,idSender);
                    },
        ()->{
            var user1=new UserFriendzone();
            user1.setUserID(idFriendRequestReceived);
            user1.setFriendRequestList(List.of(idSender));
            friendzoneRepository.save(user1);
            addRequestSentList(idFriendRequestReceived,idSender);}
        );

        if(String.valueOf(response).equals("Already Sent")){
            return ResponseEntity.badRequest().body(String.valueOf(response));
        }
        else {
            return ResponseEntity.ok().body(String.valueOf(response));
        }
    }

    public AtomicReference<String> declineFriendRequest(String idFriendRequest, String token){
        AtomicReference<String> response= new AtomicReference<>("Request Deleted");
        if(!userRepository.existsById(idFriendRequest)){
            response.set("User Doesn't Exist");
            return response;
        }
        String idUser=findProfileByToken(token).getId();
        friendzoneRepository.findByUserID(idUser).ifPresentOrElse(userFriendzone -> {
            var newList=userFriendzone.getFriendRequestList();
            if(newList!=null){
                if (!newList.contains(idFriendRequest)){
                    response.set("Request already not found");
                    return;
                }else {
                    boolean a=newList.remove(idFriendRequest);
                    System.out.println(a);
                    System.out.println(newList);
                    userFriendzone.setFriendRequestList(newList);
                    friendzoneRepository.save(userFriendzone);
                    deleteRequestSentList(idFriendRequest, idUser);
                }
            }if(newList==null){
                response.set("Request already not found");
            }
        },
                ()->{
                    response.set("Request already not found");

                }
        );
        return response;
    }


    public void addRequestSentList(String idFriendRequestSent, String idSender){

        friendzoneRepository.findByUserID(idSender).ifPresentOrElse(userFriendzone -> {
            var newList=userFriendzone.getRequestSentList();
            if(newList==null){
                newList=List.of(idFriendRequestSent);
            }else {
                newList.add(idFriendRequestSent);
            }
            userFriendzone.setRequestSentList(newList);
            friendzoneRepository.save(userFriendzone);
        }, ()->{

            var user1=new UserFriendzone();
            user1.setUserID(idSender);
            user1.setRequestSentList(List.of(idFriendRequestSent));
            friendzoneRepository.save(user1);
    });
    }

    public void deleteRequestSentList(String idFriend, String idUser){

        friendzoneRepository.findByUserID(idFriend).ifPresentOrElse(userFriendzone -> {
                    var newList=userFriendzone.getRequestSentList();
                    if(newList!=null){
                        if (!newList.contains(idUser)){
                            return;
                        }
                        boolean a=newList.remove(idUser);
                        userFriendzone.setRequestSentList(newList);
                        friendzoneRepository.save(userFriendzone);
                    }
                },
                ()->{});
    }

    public AtomicReference<String> cancelRequestSent(String idFriend, String token){
        if(!userRepository.existsById(idFriend)){
            return new AtomicReference<>("User Doesn't Exist");

        }
        String id1=findProfileByToken(token).getId();
        UserProfile user1=findProfileById(idFriend);
        String newToken= jwtService.generateToken(user1);

        return declineFriendRequest(id1,"Bearer "+newToken.trim());
    }

    public ResponseEntity<String> acceptRequest(String idFriend, String token){
        if(!userRepository.existsById(idFriend)){
            return ResponseEntity.badRequest().body("User Doesn't Exist");

        }
        UserProfile user=findProfileByToken(token);
        var verif= verifyFriendShip(idFriend,token).getBody();
        //System.out.println(verif);
        AtomicReference<FriendShipStatut> statut= new AtomicReference<>(FriendShipStatut.SENDYOUREQUEST);
         var a=String.valueOf(verif);
         var b=String.valueOf(statut);
        System.out.println(b);
        System.out.println(a.equals(b));
        if (a.equals(b)){
            declineFriendRequest(idFriend,token);
            deleteRequestSentList(user.getId(),idFriend);
            UserProfile user1=findProfileById(idFriend);
            String newToken= jwtService.generateToken(user1);
            addUserToFriendList(user.getId(),"Bearer "+newToken.trim());
            addUserToFriendList(idFriend, token);
            return ResponseEntity.ok().body("Friend Added");
        }
        return ResponseEntity.badRequest().body("Something went wrong");
    }

    //ADD TO ONE USER LIST SIDE
    public AtomicReference<String> addUserToFriendList(String idFriend, String token){
        AtomicReference<String> response= new AtomicReference<>("Friend added");
        UserProfile user= findProfileByToken(token);
        System.out.println(userRepository.findById(idFriend));
        if (idFriend.startsWith(user.getId())){
            response.set("Wrong request");
            return response;
        }

        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(
                userFriendzone1 -> {

                    var newList=userFriendzone1.getFriendList();
                    if(newList!=null){
                        if (newList.contains(idFriend)){
                            response.set("Already Friends");
                            return;
                        }
                        System.out.println("test");
                        newList.add(idFriend);
                        userFriendzone1.setFriendList(newList);
                        friendzoneRepository.save(userFriendzone1);
                    } else {
                        newList=List.of(idFriend);
                        userFriendzone1.setFriendList(newList);
                        friendzoneRepository.save(userFriendzone1);
                    }

                },
                ()->{
                    var user1=new UserFriendzone();
                    user1.setUserID(user.getId());
                    user1.setFriendList(List.of(idFriend));
                    friendzoneRepository.save(user1);
                }
        );
        return response;
    }

    public String deleteFriend(String idFriend,String token){
        if(!userRepository.existsById(idFriend)){
            return ("User Doesn't Exist");
        }
        if(!deleteFromListFriend(idFriend,token).equals("Friend Deleted")){
            return "Something went Wrong";
        }
        UserProfile user=findProfileByToken(token);
        UserProfile user1=findProfileById(idFriend);
        String newToken= jwtService.generateToken(user1);
        return deleteFromListFriend(user.getId(),"Bearer "+newToken.trim());
    }

    public String deleteFromListFriend(String idFriend, String token){
        AtomicReference<String> response= new AtomicReference<>("User not Found");
        UserProfile user= findProfileByToken(token);
        System.out.println(userRepository.findById(idFriend));

        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(userFriendzone -> {
                    var newList=userFriendzone.getFriendList();
                    if(newList!=null){
                        if (!newList.contains(idFriend)){
                            //response.set("User not Found");
                            return;
                        }
                        boolean a=newList.remove(idFriend);
                        response.set("Friend Deleted");
                        userFriendzone.setFriendList(newList);
                        friendzoneRepository.save(userFriendzone);
                    }
        },
        ()->{
            //response.set("User not Found");
        });
        return String.valueOf(response);
    }


    public ResponseEntity<FriendShipStatut> verifyFriendShip(String id, String token){

        if(!userRepository.existsById(id)){
            return ResponseEntity.badRequest().body(FriendShipStatut.NOTHING);
        }
        UserProfile user=findProfileByToken(token);
        //UserProfile ss=findProfileById(id);
        AtomicReference<FriendShipStatut> response= new AtomicReference<>(FriendShipStatut.NOTHING);

        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(userFriendzone -> {
                    var newList=userFriendzone.getFriendList();
                    if (newList!=null){
                        if (newList.contains(id)){
                            response.set(FriendShipStatut.FRIENDS);
                        }
                    }

                    newList=userFriendzone.getFriendRequestList();
                    if (newList!=null){
                        if (newList.contains(id)){
                            response.set(FriendShipStatut.SENDYOUREQUEST);
                        }
                    }

                    newList=userFriendzone.getRequestSentList();
                    if (newList!=null){
                        if (newList.contains(id)){
                            response.set(FriendShipStatut.WAITINGFORACCEPT);
                        }
                    }

                    newList=userFriendzone.getBlockedUser();
                    if (newList!=null){
                        if (newList.contains(id)){
                            response.set(FriendShipStatut.BLOCKED);
                        }
                    }

        },
                ()->{});
        friendzoneRepository.findByUserID(id).ifPresentOrElse(userFriendzone -> {
                    var newList=userFriendzone.getBlockedUser();
                    if (newList!=null){
                        if (newList.contains(user.getId())){
                            response.set(FriendShipStatut.BLOCKEDYOU);
                        }
                    }
        },
                ()->{});
        return ResponseEntity.ok().body(response.get());
    }

    public ResponseEntity<String> blockUser(String idFriend, String token){

        if(!userRepository.existsById(idFriend)){
            return ResponseEntity.badRequest().body("User Doesn't Exist");
        }

        var verif= verifyFriendShip(idFriend,token).getBody();
        var a=String.valueOf(verif);
        System.out.println(verif);
        AtomicReference<FriendShipStatut> statut3= new AtomicReference<>(FriendShipStatut.BLOCKED);
        var e=String.valueOf(statut3);
        if(a.equals(e)){
            return ResponseEntity.badRequest().body("Already Blocked");
        }

        AtomicReference<FriendShipStatut> statut= new AtomicReference<>(FriendShipStatut.SENDYOUREQUEST);
        var b=String.valueOf(statut);
        if (a.equals(b)){
            declineFriendRequest(idFriend, token);
            addUserToBlockedList(idFriend, token);
            return ResponseEntity.ok().body("User Blocked");
        }
        AtomicReference<FriendShipStatut> statut1= new AtomicReference<>(FriendShipStatut.WAITINGFORACCEPT);
        var c=String.valueOf(statut1);
        if (a.equals(c)){
            cancelRequestSent(idFriend, token);
            addUserToBlockedList(idFriend, token);
            return ResponseEntity.ok().body("User Blocked");
        }

        AtomicReference<FriendShipStatut> statut2= new AtomicReference<>(FriendShipStatut.FRIENDS);
        var d=String.valueOf(statut2);
        if (a.equals(d)){
            deleteFriend(idFriend, token);
            addUserToBlockedList(idFriend, token);
            return ResponseEntity.ok().body("User Blocked");
        }
        addUserToBlockedList(idFriend, token);
        return ResponseEntity.ok().body("User Blocked");
    }

    public AtomicReference<String> addUserToBlockedList(String idFriend, String token){
        AtomicReference<String> response= new AtomicReference<>("User Blocked");
        if(!userRepository.existsById(idFriend)){
            response.set("User Doesn't Exist");
            return response;
        }
        UserProfile user= findProfileByToken(token);
        //System.out.println(userRepository.findById(idFriend));
        if (idFriend.equals(user.getId())){
            response.set("Wrong request");
            return response;
        }
        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(
                userFriendzone1 -> {

                    var newList=userFriendzone1.getBlockedUser();
                    if(newList!=null){
                        if (newList.contains(idFriend)){
                            response.set("Already Blocked");
                            return;
                        }
                        newList.add(idFriend);
                        userFriendzone1.setBlockedUser(newList);
                        friendzoneRepository.save(userFriendzone1);
                    } else {
                        newList=List.of(idFriend);
                        userFriendzone1.setBlockedUser(newList);
                        friendzoneRepository.save(userFriendzone1);
                    }

                },
                ()->{
                    var user1=new UserFriendzone();
                    user1.setUserID(user.getId());
                    user1.setBlockedUser(List.of(idFriend));
                    friendzoneRepository.save(user1);
                }
        );
        return response;

    }

    public ResponseEntity<String> unblockUser(String idFriend, String token){
        AtomicReference<String> response= new AtomicReference<>("User Unblocked");
        UserProfile user= findProfileByToken(token);
        System.out.println(userRepository.findById(idFriend));

        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(userFriendzone -> {
                    var newList=userFriendzone.getBlockedUser();
                    if(newList!=null){
                        if (!newList.contains(idFriend)){
                            response.set("User not Found");
                            return;
                        }
                        boolean a=newList.remove(idFriend);
                        userFriendzone.setBlockedUser(newList);
                        friendzoneRepository.save(userFriendzone);
                    }
                },
                ()->{
                    response.set("User not Found");
                });
        if(response.get().equals("User not Found")){
            return ResponseEntity.badRequest().body(response.get());
        }
        return ResponseEntity.ok().body(response.get());
    }


    public List<UserProfile> getFriendList(String idUser,String token){
        //UserProfile user= findProfileByToken(token);
        List<UserProfile> friendList=new ArrayList<>();

        friendzoneRepository.findByUserID(idUser).ifPresentOrElse(userFriendzone1 -> {
            friendList.addAll(userRepository.findAllById(userFriendzone1.getFriendList()));},
                ()->{
            return;
                }

        );
        return friendList;
    }

    public List<UserProfile> getRequestList(String token){
        UserProfile user= findProfileByToken(token);
        List<UserProfile> friendList=new ArrayList<>();

        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(userFriendzone1 -> {
                    friendList.addAll(userRepository.findAllById(userFriendzone1.getFriendRequestList()));},
                ()->{
                    return;
                }

        );
        return friendList;
    }

    public List<UserProfile> getRequestSentList(String token){
        UserProfile user= findProfileByToken(token);
        List<UserProfile> friendList=new ArrayList<>();

        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(userFriendzone1 -> {
                    friendList.addAll(userRepository.findAllById(userFriendzone1.getRequestSentList()));},
                ()->{
                    return;
                }

        );
        return friendList;
    }


    public List<UserProfile> getBlockedList(String token){
        UserProfile user= findProfileByToken(token);
        List<UserProfile> friendList=new ArrayList<>();

        friendzoneRepository.findByUserID(user.getId()).ifPresentOrElse(userFriendzone1 -> {
                    friendList.addAll(userRepository.findAllById(userFriendzone1.getBlockedUser()));},
                ()->{
                    return;
                }

        );
        return friendList;
    }

    public List<UserProfile> getSuggestionList(String token){
        List<UserProfile> suggestionList=new ArrayList<>();

        userRepository.findAll().forEach(userProfile -> {
            if(verifyFriendShip(userProfile.getId(),token).getBody().equals(FriendShipStatut.NOTHING)){
                suggestionList.add(userProfile);
            }
        });
        suggestionList.remove(findProfileByToken(token));
        return suggestionList;

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
