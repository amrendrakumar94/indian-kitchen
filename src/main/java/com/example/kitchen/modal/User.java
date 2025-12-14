package com.example.kitchen.modal;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_details")
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "create_date")
    Timestamp createDate;

    @Column(name = "name")
    String name;

    @Column(name = "phone_no")
    String phoneNo;

    @Column(name = "email")
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "reservation_ids")
    String reservationId;

    @Column(name = "is_logged_in")
    boolean isLoggedIn;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return phoneNo;
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
}
