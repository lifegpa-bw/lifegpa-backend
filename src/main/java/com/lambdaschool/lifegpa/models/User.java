package com.lambdaschool.lifegpa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lambdaschool.lifegpa.logging.Loggable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

// User is considered the parent entity

@Loggable
@Entity
@Table(name = "users")
public class User extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;

    @Column(nullable = false,
            unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false,
            unique = true)
    @Email
    private String email;

    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<UserRoles> userroles = new ArrayList<>();

    @OneToMany(mappedBy = "user",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Useremail> useremails = new ArrayList<>();

    @OneToMany(mappedBy = "user",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<DailyTracker> dailytrackers = new ArrayList<>();

    public List<DailyTracker> getDailytrackers() {
        return dailytrackers;
    }

    public void setDailytrackers(List<DailyTracker> dailytrackers) {
        this.dailytrackers = dailytrackers;
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
    }

    @OneToMany(mappedBy = "user",
                cascade = CascadeType.ALL,
                orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Habit> habits = new ArrayList<>();

    public User(String username, String password, @Email String email, List<UserRoles> userroles, List<Useremail> useremails, List<DailyTracker> dailytrackers, List<Habit> habits) {
        setUsername(username);
        setPassword(password);
        this.email = email;
        for (UserRoles ur : userroles)
        {
            ur.setUser(this);
        }
        this.userroles = userroles;
        this.useremails = useremails;
        this.dailytrackers = dailytrackers;
        this.habits = habits;
    }

    public User()
    {
    }

//    public User(String username,
//                String password,
//                String email,
//                List<UserRoles> userRoles)
//    {
//        setUsername(username);
//        setPassword(password);
//        this.email = email;
//        for (UserRoles ur : userRoles)
//        {
//            ur.setUser(this);
//        }
//        this.userroles = userRoles;
//    }

    public long getUserid()
    {
        return userid;
    }

    public void setUserid(long userid)
    {
        this.userid = userid;
    }

    public String getUsername()
    {
        if (username == null) // this is possible when updating a user
        {
            return null;
        } else
        {
            return username.toLowerCase();
        }
    }

    public void setUsername(String username)
    {
        this.username = username.toLowerCase();
    }

    public String getEmail()
    {
        if (email == null) // this is possible when updating a user
        {
            return null;
        } else
        {
            return email.toLowerCase();
        }
    }

    public void setEmail(String email)
    {
        this.email = email.toLowerCase();
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setPasswordNoEncrypt(String password)
    {
        this.password = password;
    }

    public List<UserRoles> getUserroles()
    {
        return userroles;
    }

    public void setUserroles(List<UserRoles> userroles)
    {
        this.userroles = userroles;
    }

    public List<Useremail> getUseremails()
    {
        return useremails;
    }

    public void setUseremails(List<Useremail> useremails)
    {
        this.useremails = useremails;
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority()
    {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();

        for (UserRoles r : this.userroles)
        {
            String myRole = "ROLE_" + r.getRole()
                                       .getName()
                                       .toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myRole));
        }

        return rtnList;
    }

//    @Override
//    public String toString()
//    {
//        return "User{" + "userid=" + userid + ", username='" + username + '\'' + ", password='" + password + '\'' + ", email='" + email + '\'' + ", userroles=" + userroles + ", useremails=" + useremails + '}';
//    }
}
