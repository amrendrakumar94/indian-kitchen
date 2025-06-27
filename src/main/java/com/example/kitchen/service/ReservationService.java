package com.example.kitchen.service;

import com.example.kitchen.dao.ReservationDao;
import com.example.kitchen.modal.ReservationDetails;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationDao reservationDao;
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

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
            logger.error("error in getReservationDetails()!", e);
        }
        return reservationJsonObject;
    }
}
