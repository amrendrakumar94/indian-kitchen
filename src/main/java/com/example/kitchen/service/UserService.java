package com.example.kitchen.service;

import com.example.kitchen.dao.DishDao;
import com.example.kitchen.dao.UserDao;
import com.example.kitchen.dto.CartDetailDto;
import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.DishDetails;
import com.example.kitchen.modal.OrderedDetails;
import com.example.kitchen.modal.User;
import com.example.kitchen.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    private final DishDao dishDao;

    public List<Integer> addToCart(JSONObject jsonObject) {
        List<Integer> dishIds = new ArrayList<>();
        try {
            if (jsonObject != null && !jsonObject.isEmpty()) {
                int dishId = jsonObject.has("itemId") ? jsonObject.getInt("itemId") : 0;
                int userId = jsonObject.has("userId") ? jsonObject.getInt("userId") : 0;
                CartDetails cartDetails = new CartDetails();
                if (dishId > 0 && userId > 0) {
                    cartDetails.setQuantity(1);
                    cartDetails.setUserId(userId);
                    cartDetails.setDishId(dishId);
                    userDao.addToCart(cartDetails);
                    List<CartDetails> cartDetailsList = userDao.getDishIds(userId);
                    if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                        for (CartDetails obj : cartDetailsList) {
                            if (obj != null) {
                                dishIds.add(obj.getDishId());
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.error("Error in addToCart() ", e);
        }
        return dishIds;
    }

    public List<CartDetailDto> getDishDetailsLisByForCart(int userId) {

        try {
            List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
            if (userId > 0) {
                List<CartDetails> cartDetailsList = userDao.getCartDetailsByUserId(userId);
                if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                    for (CartDetails obj : cartDetailsList) {
                        CartDetailDto cartDetailDto = new CartDetailDto();
                        if (obj != null) {
                            DishDetails dishDetails = dishDao.getDishById(obj.getDishId());
                            if (dishDetails != null) {
                                cartDetailDto.setDishDetail(dishDetails);
                                cartDetailDto.setCount(obj.getQuantity());
                                cartDetailDtoList.add(cartDetailDto);
                            }
                        }
                    }
                }
            }
            return cartDetailDtoList;
        } catch (Exception e) {
            log.error("Error in getDishDetailsLisByUserId()!", e);
        }
        return null;
    }

    public boolean deleteItem(int userId, int dishId) {
        try {
            if (userId > 0 && dishId > 0) {
                return userDao.deleteItem(userId, dishId);
            }

        } catch (Exception e) {
            log.error("Error in deleteItem()! ", e);
        }
        return false;
    }

    public boolean incrementItem(int userId, int dishId) {
        try {
            if (userId > 0 && dishId > 0) {
                CartDetails cartDetails = userDao.getCartDetails(dishId, userId);
                if (cartDetails != null) {
                    cartDetails.setQuantity(cartDetails.getQuantity() + 1);
                }
                return userDao.addToCart(cartDetails);
            }

        } catch (Exception e) {
            log.error("Error in incrementItem(). ", e);
        }
        return false;
    }

    public boolean decrementItem(int userId, int dishId) {
        try {
            if (userId > 0 && dishId > 0) {
                CartDetails cartDetails = userDao.getCartDetails(dishId, userId);
                if (cartDetails != null && cartDetails.getQuantity() > 1) {
                    cartDetails.setQuantity(cartDetails.getQuantity() - 1);
                    return userDao.addToCart(cartDetails);
                }
            }

        } catch (Exception e) {
            log.error("Error in incrementItem(). ", e);
        }
        return false;
    }

    public boolean orderItems(int userId) {
        try {
            if (userId > 0) {
                List<CartDetails> cartDetailsList = userDao.getCartDetailsByUserId(userId);
                if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                    for (CartDetails cartDetails : cartDetailsList) {
                        OrderedDetails orderedDetails = new OrderedDetails();
                        if (cartDetails != null) {
                            orderedDetails.setUserId(cartDetails.getUserId());
                            orderedDetails.setCreateDate(CommonUtils.getCurrentTimestamp());
                        }
                    }
                }
                return userDao.deleteAllItem(userId);
            }

        } catch (Exception e) {
            log.error("Error in deleteItem()! ", e);
        }
        return false;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
