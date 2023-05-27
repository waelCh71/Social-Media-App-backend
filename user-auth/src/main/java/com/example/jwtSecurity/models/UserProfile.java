package com.example.jwtSecurity.models;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "Users_Profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityScan
public class UserProfile implements UserDetails {

    @Id
    private String id;

    private String firstname;

    private  String lastname;

    private String fullName;


    private  String email;
    private  String password;

    private String dob;

    private String country;

    private String phoneNumber;

    private String city;

    private String bio;

    private UserStatut userStatut;

    private ProfileState profileState;

    private String profile_pic_url;

    private String cover_pic_url;

    private List<String> userInterests;

    private Date createdDate;

    //private UserFriendzone userFriendzone;

    private Role role;

    private String gender;


    //private List<Token> tokens;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //Constructor for Authentication
    public UserProfile(String firstName, String lastname, String fullName, String email,
                       String password, String dob, String country, String phoneNumber,
                       String city, Date createdDate, Role role, String gender) {
        this.firstname = firstName;
        this.lastname = lastname;
        this.fullName=fullName;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.createdDate = createdDate;
        this.role = role;
        this.gender=gender;
    }
    //Constructor for completing Info


    public String getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Role getRole() {
        return role;
    }

    public String getDob() {
        return dob;
    }

    public String getCountry() {
        return country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public UserStatut getUserStatut() {
        return userStatut;
    }

    public ProfileState getProfileState() {
        return profileState;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public String getCover_pic_url() {
        return cover_pic_url;
    }

    public List<String> getUserInterests() {
        return userInterests;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    /*public List<Token> getTokens() {
        return tokens;
    }*/

}
