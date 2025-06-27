package com.example.kitchen.modal;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_details")
@Data
public class UserDetails {
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
}
