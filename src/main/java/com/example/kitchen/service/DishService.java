package com.example.kitchen.service;

import com.example.kitchen.dao.DishDao;
import com.example.kitchen.dao.UserDao;
import com.example.kitchen.dto.DishDetailsDto;
import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.DishDetails;
import com.example.kitchen.util.CommonUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishService {
    @Autowired
    private DishDao dishDao;

    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(DishService.class);

    public DishDetailsDto getAllDishes(int userId, String cuisineType) {
        DishDetailsDto dishDetailsDto = new DishDetailsDto();
        try {
            List<DishDetails> dishDetailsList;
            List<CartDetails> cartDetailsList;
            List<Integer> dishIds = new ArrayList<>();
            if (!"null".equalsIgnoreCase(cuisineType) && CommonUtils.isNotNullAndNotEmpty(cuisineType) && !"All".equalsIgnoreCase(cuisineType)) {
                dishDetailsList = dishDao.getDishDetailsList(cuisineType);
                cartDetailsList = userDao.getDishIds(userId);
                if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                    for (CartDetails obj : cartDetailsList) {
                        if (obj != null) {
                            dishIds.add(obj.getDishId());
                        }
                    }
                }
            } else {
                dishDetailsList = dishDao.getAllDishes();
                cartDetailsList = userDao.getDishIds(userId);
                if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                    for (CartDetails obj : cartDetailsList) {
                        if (obj != null) {
                            dishIds.add(obj.getDishId());
                        }
                    }
                }
            }
            dishDetailsDto.setDishDetails(dishDetailsList);
            dishDetailsDto.setDishIds(dishIds);
        } catch (Exception e) {
            logger.error("Error in getAllDishes(). ", e);
        }
        return dishDetailsDto;
    }

    public List<DishDetails> getDishDetailsByDishIds(String dishIds) {
        try {
            List<DishDetails> dishDetailsList = dishDao.getDishDetailsByDishIds(dishIds);
            return dishDetailsList;
        } catch (Exception e) {
            logger.error("Error in getDishDetailsByDishIds(). ", e);
        }
        return null;
    }
}
