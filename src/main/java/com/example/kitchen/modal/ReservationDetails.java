package com.example.kitchen.modal;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;


@Data
@Entity
@Table(name = "reservation_details")
public class ReservationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;


    @Column(name = "booking_date")
    Timestamp bookingDate;

    @Column(name = "create_date")
    Timestamp createDate;

    @Column(name = "slot_time")
    Timestamp slotTime;

    @Column(name = "last_modification")
    Timestamp lastModification;

    @Column(name = "status")
    String status;

    @Column(name = "user_id")
    int userId;

    @Column(name = "guest_count")
    int guestCount;

    @Column(name = "price")
    int price;

}
