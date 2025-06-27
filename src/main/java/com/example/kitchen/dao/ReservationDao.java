package com.example.kitchen.dao;

import com.example.kitchen.modal.ReservationDetails;

import java.util.List;

public interface ReservationDao {
    List<ReservationDetails> getReservationDetailsByUserId(int userId);
}
