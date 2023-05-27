package com.example.userservices.Services;

import com.example.jwtSecurity.models.UserProfile;
import com.example.jwtSecurity.models.UserStatut;
import com.example.userservices.config.JwtService;
import com.example.userservices.models.DeletedUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final DeletedUserRepository deletedUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final UserFriendzoneRepository friendzoneRepository;


    //TODO COMPLETE BIO AND CITY
    public UserProfile completeAllUserInfo(UserProfile request, String token){
        UserProfile user= findProfileByToken(token);
        user.setUserInterests(request.getUserInterests());

        return userRepository.save(user);
    }



    public UserProfile updateUserName(String newFirstName, String newLastName, String password, String token)  {

        try {
            UserProfile user=findProfileByToken(token);

            if(passwordEncoder.matches(password, user.getPassword())){
                user.setFirstname(newFirstName);
                user.setLastname(newLastName);
                user.setFullName(newFirstName+newLastName);
                System.out.println("User Name updated");
                userRepository.save(user);
                return user;
            }
            else {
                throw new Exception("PASSWORD INCORRECT");
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public UserProfile updateUserPhoneNumber(String phone,String token){
        UserProfile user=findProfileByToken(token);
        user.setPhoneNumber(phone);
        return userRepository.save(user);
    }

    public UserProfile updateProfilePic(String url,String token){
        UserProfile user=findProfileByToken(token);
        user.setProfile_pic_url(url);
        return userRepository.save(user);
    }

    public UserProfile updateCoverPic(String url,String token){
        UserProfile user=findProfileByToken(token);
        user.setCover_pic_url(url);
        return userRepository.save(user);
    }


    public UserProfile updateUserInfo(String newCountry,String newCity,String newDOB,String newBio,String token){
        UserProfile user= findProfileByToken(token);

        if (newCountry!=null){user.setCountry(newCountry);}
        if (newCity!=null){user.setCity(newCity);}
        if (newDOB!=null){user.setDob(newDOB);}
        if (newBio!=null){user.setBio(newBio);}
        return userRepository.save(user);
    }

    //TODO ID-USER
    public UserProfile getuser(String id){
        return userRepository.findById(id).orElseThrow();
    }

    public ResponseEntity<List<UserProfile>> getAllProfileUser(){
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    public List<UserProfile> getAllUser(){
        return userRepository.findAll();
    }

    public List<UserProfile> findUserByName(String name){

        Map<String,String> allUserFirstNames= new HashMap<>();
        Map<String,String> allUserLastNames=new HashMap<>();
        Map<String,String> allUserFullNames=new HashMap<>();

        List<UserProfile> users=new ArrayList<>();
        var allUser= getAllUser();
        allUser.forEach(t->{
            allUserFirstNames.put(t.getId(),t.getFirstname().toLowerCase());
            allUserLastNames.put(t.getId(),t.getLastname().toLowerCase());
            allUserFullNames.put(t.getId(),t.getFullName().toLowerCase());
        });

            allUserFirstNames.forEach((k, v)->{
                if (v.equals(name.toLowerCase()) || v.startsWith(name.toLowerCase())
                        || v.contains(name.toLowerCase()) ){
                    users.add(userRepository.findById(k).orElseThrow());
                }
            });


            allUserLastNames.forEach((k, v)->{
                if (v.equals(name.toLowerCase()) || v.startsWith(name.toLowerCase())
                        || v.contains(name.toLowerCase()) ){
                    users.add(userRepository.findById(k).orElseThrow());
                }
            });

            allUserFullNames.forEach((k, v)->{
                if (v.equals(name.toLowerCase()) || v.startsWith(name.toLowerCase())
                        || v.contains(name.toLowerCase())){
                    UserProfile user=userRepository.findById(k).orElseThrow();
                    if (!users.contains(user)){
                        users.add(user);
                    }
                }
            });

        return users;
    }

    public List<UserProfile> findUserByCity(String city){
        Map<String,String> allUserCity= new HashMap<>();


        List<UserProfile> users=new ArrayList<>();
        var allUser= getAllUser();
        allUser.forEach(t->{
            allUserCity.put(t.getId(),t.getCity().toLowerCase());
        });

        allUserCity.forEach((k, v)->{
            if (v.equals(city.toLowerCase()) || v.startsWith(city.toLowerCase())
                    || v.contains(city.toLowerCase()) ){
                users.add(userRepository.findById(k).orElseThrow());
            }
        });
        return users;
    }

    public List<UserProfile> findUserByPhone(long phone){
        return userRepository.findByPhoneNumber(phone).orElseThrow();
    }

    public List<UserProfile> filterUserByNameAndCity(String name,String city){
        List<UserProfile> user1=findUserByName(name);
        List<UserProfile> filtredUser=new ArrayList<>();
        if (city==null){return user1;}
        else {
            user1.forEach(s->{
                if(findUserByCity(city).contains(s)){
                    filtredUser.add(s);
                }
            });
            return filtredUser;
        }
    }

    public ResponseEntity<String> deleteProfile(String password, String token){
        DeletedUsers deletedUser=new DeletedUsers();
        UserProfile user=findProfileByToken(token);

        if (!passwordEncoder.matches(password, user.getPassword())){
            return ResponseEntity.badRequest().body("Password Incorrect");
        }

        deletedUser.setId(user.getId());
        deletedUser.setFirstname(user.getFirstname());
        deletedUser.setLastname(user.getLastname());
        deletedUser.setFullName(user.getFullName());
        deletedUser.setEmail(user.getEmail());
        deletedUser.setPassword(user.getPassword());
        deletedUser.setCountry(user.getCountry());
        deletedUser.setDob(user.getDob());
        deletedUser.setCity(user.getCity());
        deletedUser.setPhoneNumber(user.getPhoneNumber());
        deletedUser.setProfileState(user.getProfileState());
        deletedUser.setUserStatut(UserStatut.OFFLINE);
        deletedUser.setProfile_pic_url(user.getProfile_pic_url());
        deletedUser.setCover_pic_url(user.getCover_pic_url());
        deletedUser.setCreatedDate(user.getCreatedDate());
        deletedUser.setRole(user.getRole());
        deletedUser.setUserInterests(user.getUserInterests());
        deletedUser.setBio(user.getBio());
        friendzoneRepository.findByUserID(user.getId()).ifPresent(userFriendzone -> {
            deletedUser.setUserFriendzone(userFriendzone);
        });
        //TODO COMLETE USER ATTRIBUTE
        Date deletingDate =new Date(System.currentTimeMillis());
        deletedUser.setDeletedDate(deletingDate);


        //TODO COMPLETE DELETING USER_COLLECTIONS
        deletedUserRepository.insert(deletedUser);
        System.out.println("3");
        userRepository.deleteById(user.getId());
        return ResponseEntity.ok().body("User Deleted Succefuly");
    }

    public ResponseEntity<String> setUserStatut (String token){

        UserProfile user= findProfileByToken(token);
        if(user.getUserStatut().equals(UserStatut.ONLINE)){
            user.setUserStatut(UserStatut.OFFLINE);
        }else {
            user.setUserStatut(UserStatut.ONLINE);
        }
        userRepository.save(user);
        return ResponseEntity.ok().body(user.getUserStatut().toString());
    }


    public UserProfile findProfileByToken(String token){
        var jwtToken=token.substring(7);
        var email=jwtService.extractUserName(jwtToken);
        return userRepository.findByEmail(email).orElseThrow();
    }
    public ResponseEntity<UserProfile> findProfileById(String id){
        if(userRepository.existsById(id)){
            UserProfile user=userRepository.findById(id).orElseThrow();
            return ResponseEntity.ok().body(user);
        }
            return ResponseEntity.badRequest().build();


    }


}
