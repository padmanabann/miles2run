package org.miles2run.business.domain;

import org.hibernate.validator.constraints.Email;
import org.miles2run.business.bean_validation.ImageUrl;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    @Column(unique = true, updatable = false)
    @Size(max = 20)
    private String username;

    @NotNull
    @Size(max = 50)
    private String fullname;

    @NotNull
    @Size(max = 200)
    private String bio;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull
    private long goal;

    @NotNull
    @Enumerated(EnumType.STRING)
    private GoalUnit goalUnit;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "profile")
    private final List<SocialConnection> socialConnections = new ArrayList<>();

    @Temporal(TemporalType.DATE)
    @NotNull
    private final Date registeredOn = new Date();

    @ImageUrl
    private String profilePic;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Transient
    private String miniProfilePic;

    @Transient
    private String biggerProfilePic;

    public Profile() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullName) {
        this.fullname = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public List<SocialConnection> getSocialConnections() {
        return socialConnections;
    }

    public Date getRegisteredOn() {
        return registeredOn;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMiniProfilePic() {
        return getImageWithSize("mini");
    }

    public String getBiggerProfilePic() {
        return getImageWithSize("bigger");
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public GoalUnit getGoalUnit() {
        return goalUnit;
    }

    public void setGoalUnit(GoalUnit goalUnit) {
        this.goalUnit = goalUnit;
    }

    public Role getRole() {
        return role;
    }

    private String getImageWithSize(String size) {
        if (this.profilePic != null) {
            int index = this.profilePic.lastIndexOf(".");
            String imgPrefix = this.profilePic.substring(0, index);
            String picExtension = this.profilePic.substring(index);
            return new StringBuilder(imgPrefix).append("_").append(size).append(picExtension).toString();
        }
        return this.profilePic;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullname + '\'' +
                ", bio='" + bio + '\'' +
                ", city='" + city + '\'' +
                ", goal=" + goal +
                ", country='" + country + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", registeredOn=" + registeredOn +
                '}';
    }
}
