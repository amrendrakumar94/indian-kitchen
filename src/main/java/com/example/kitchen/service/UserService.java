package com.example.kitchen.service;

import com.example.kitchen.dao.DishDao;
import com.example.kitchen.dao.UserDao;
import com.example.kitchen.dto.CartDetailDto;
import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.DishDetails;
import com.example.kitchen.modal.OrderedDetails;
import com.example.kitchen.modal.UserDetails;
import com.example.kitchen.util.CommonUtils;
import com.example.kitchen.util.PasswordService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DishDao dishDao;


    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public JSONObject signUpUser(JSONObject jsonObj) {
        JSONObject json = new JSONObject();
        try {
            UserDetails userDetails = new UserDetails();
            if (jsonObj != null) {
                String firstName = jsonObj.has("first_name") ? jsonObj.getString("first_name") : "";
                String lastName = jsonObj.has("last_name") ? jsonObj.getString("last_name") : "";
                String email = jsonObj.has("email") ? jsonObj.getString("email") : "";
                String password = jsonObj.has("password") ? jsonObj.getString("password") : "";
                if (CommonUtils.isNotNullAndNotEmpty(firstName) && CommonUtils.isNotNullAndNotEmpty(lastName) && CommonUtils.isNotNullAndNotEmpty(email) && CommonUtils.isNotNullAndNotEmpty(password)) {
                    UserDetails existUser = userDao.getUserDetailsByEmail(email);
                    if (existUser != null) {
                        json.put("message", "you already have an account");
                        return json;
                    } else {
                        userDetails.setName(firstName + " " + lastName);
                        password = PasswordService.encodePassword(password);
                        userDetails.setPassword(password);
                        userDetails.setPhoneNo("");
                        userDetails.setEmail(email);
                        userDetails.setCreateDate(CommonUtils.getCurrentTimestamp());
                        if (userDao.saveUserDetails(userDetails)) json.put("message", "Account created");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("signUpUser", e);
        }
        return json;
    }

    public JSONObject userLogin(JSONObject jsonObject) {
        JSONObject json = new JSONObject();
        try {
            if (jsonObject != null) {
                String email = jsonObject.getString("email") != null ? jsonObject.getString("email") : "";
                String password = jsonObject.getString("password") != null ? jsonObject.getString("password") : "";
                UserDetails userDetails = userDao.getUserDetailsByEmail(email);
                if (userDetails != null) {
                    if (PasswordService.matchPassword(password, userDetails.getPassword())) {
                        if (!userDetails.isLoggedIn()) {
                            userDetails.setLoggedIn(true);
                        }
                        userDao.saveUserDetails(userDetails);
                        json = JSONObject.fromObject(userDetails);
                    } else {
                        json.put("message", "Email and password does not match");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error in userLogin()!", e);
        }
        return json;
    }

    public List<Integer> addToCart(JSONObject jsonObject) {
        List<Integer> dishIds = new ArrayList<>();
        try {
            if (jsonObject != null && !jsonObject.isEmpty()) {
                int dishId = jsonObject.has("itemId") ? jsonObject.getInt("itemId") : 0;
                int userId = jsonObject.has("userId") ? jsonObject.getInt("userId") : 0;
                CartDetails cartDetails = new CartDetails();
                if (dishId > 0 && userId > 0) {
                    cartDetails.setCount(1);
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
            logger.error("Error in addToCart()!.", e);
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
                                cartDetailDto.setCount(obj.getCount());
                                cartDetailDtoList.add(cartDetailDto);
                            }
                        }
                    }
                }
            }
            return cartDetailDtoList;
        } catch (Exception e) {
            logger.error("Error in getDishDetailsLisByUserId()!", e);
        }
        return null;
    }


    public boolean deleteItem(int userId, int dishId) {
        try {
            if (userId > 0 && dishId > 0) {
                return userDao.deleteItem(userId, dishId);
            }

        } catch (Exception e) {
            logger.error("Error in deleteItem()! ", e);
        }
        return false;
    }

    public boolean incrementItem(int userId, int dishId) {
        try {
            if (userId > 0 && dishId > 0) {
                CartDetails cartDetails = userDao.getCartDetails(dishId, userId);
                if (cartDetails != null) {
                    cartDetails.setCount(cartDetails.getCount() + 1);
                }
                return userDao.addToCart(cartDetails);
            }

        } catch (Exception e) {
            logger.error("Error in incrementItem(). ", e);
        }
        return false;
    }

    public boolean decrementItem(int userId, int dishId) {
        try {
            if (userId > 0 && dishId > 0) {
                CartDetails cartDetails = userDao.getCartDetails(dishId, userId);
                if (cartDetails != null && cartDetails.getCount() > 1) {
                    cartDetails.setCount(cartDetails.getCount() - 1);
                    return userDao.addToCart(cartDetails);
                }
            }

        } catch (Exception e) {
            logger.error("Error in incrementItem(). ", e);
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
            logger.error("Error in deleteItem()! ", e);
        }
        return false;
    }
}
