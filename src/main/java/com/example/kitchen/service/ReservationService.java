package com.example.kitchen.service;

import com.example.kitchen.dao.ReservationDao;
import com.example.kitchen.modal.ReservationDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationDao reservationDao;

    public JSONObject getReservationDetails(JSONObject jsonObj) {
        JSONObject reservationJsonObject = new JSONObject();
        try {
            List<ReservationDetails> reservationDetailsList = new ArrayList<>();
            if (jsonObj != null) {
                int userId = jsonObj.getInt("user_id");
                if (userId > 0) {
                    reservationDetailsList = reservationDao.getReservationDetailsByUserId(userId);
                    reservationJsonObject = JSONObject.fromObject(reservationDetailsList);
                }
            }

        } catch (Exception e) {
            log.error("error in getReservationDetails()!", e);
        }
        return reservationJsonObject;
    }
}
