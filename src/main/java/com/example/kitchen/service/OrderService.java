package com.example.kitchen.service;

import com.example.kitchen.dao.OrderDao;
import com.example.kitchen.modal.OrderedDetails;
import com.example.kitchen.util.CommonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public JSONArray getOrderList(int userId) {
        JSONArray jsonArray = new JSONArray();
        try {
            List<OrderedDetails> orderedDetailsList = new ArrayList<>();
            if (userId > 0) {
                orderedDetailsList = orderDao.getAllOrderByUserId(userId);
                jsonArray = JSONArray.fromObject(orderedDetailsList);
            }
        } catch (Exception e) {
            logger.error("error in getOrderList()!", 2);
        }
        return jsonArray;
    }

    public boolean saveOrder(JSONObject jsonObj) {
        try {
            if (jsonObj != null) {
                int userId = jsonObj.has("user_id") ? jsonObj.getInt("user_id") : 0;
                String address = jsonObj.has("address") ? jsonObj.getString("address") : "";
                String orderIds = jsonObj.has("order_ids") ? jsonObj.getString("order_ids") : "";

                if (userId > 0 && CommonUtils.isNotNullAndNotEmpty(address) && CommonUtils.isNotNullAndNotEmpty(orderIds)) {
                    List<OrderedDetails> orderedDetailList = orderDao.getAllOrderByUserId(userId);
                    if(orderedDetailList!=null && !orderedDetailList.isEmpty()){
                        
                    }
                    OrderedDetails orderedDetails = new OrderedDetails();
                    orderedDetails.setOrderIds(orderIds);
                    orderedDetails.setAddress(address);
                    orderedDetails.setUserId(userId);
                    orderedDetails.setCreateDate(CommonUtils.getCurrentTimestamp());
                    orderedDetails.setStatus("");
                    return orderDao.saveOrder(orderedDetails);
                }

            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return false;
    }
}
