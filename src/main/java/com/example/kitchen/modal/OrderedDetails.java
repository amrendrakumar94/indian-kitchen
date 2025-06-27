package com.example.kitchen.modal;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "ordered_details")
public class OrderedDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "user_id")
    int userId;

    @Column(name = "create_date")
    Timestamp createDate;

    @Column(name = "order_ids")
    String orderIds;

    @Column(name = "status")
    String status;

    @Column(name = "address")
    String address;

}
