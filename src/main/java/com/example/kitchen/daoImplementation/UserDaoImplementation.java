package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.UserDao;
import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.UserDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImplementation implements UserDao {
    private Logger logger;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public boolean saveUserDetails(UserDetails userDetails) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(userDetails);
            tx.commit();
        } catch (Exception e) {
            logger.error("error in saveUserDetails()!");
        } finally {
            if (tx != null && session != null && session.isOpen()) {
                session.close();
                tx.rollback();
            }
        }
        return true;
    }

    @Override
    public UserDetails getUserDetailsByEmail(String email) {
        Session session = null;
        Transaction tx = null;
        UserDetails userDetails = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<UserDetails> query = session.createQuery("FROM UserDetails WHERE email = :EMAIL", UserDetails.class);
            query.setParameter("EMAIL", email);
            userDetails = query.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return userDetails;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return userDetails;
    }

    @Override
    public boolean addToCart(CartDetails cartDetails) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(cartDetails);
            tx.commit();
            return true;
        } catch (Exception e) {
            logger.error("Error in ", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

    @Override
    public CartDetails getCartDetails(int dishId, int userId) {
        Session session = null;
        Transaction tx = null;
        CartDetails cartDetails = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<CartDetails> query = session.createQuery("FROM CartDetails WHERE userId = :USER_ID AND dishId =:DISH_ID", CartDetails.class);
            query.setParameter("USER_ID", userId);
            query.setParameter("DISH_ID", dishId);
            cartDetails = query.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return cartDetails;
    }

    @Override
    public List<CartDetails> getCartDetailsByUserId(int userId) {
        Session session = null;
        Transaction tx = null;
        List<CartDetails> cartDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<CartDetails> query = session.createQuery("FROM CartDetails WHERE userId = :USER_ID", CartDetails.class);
            query.setParameter("USER_ID", userId);
            cartDetailsList = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return cartDetailsList;
    }

    @Override
    public List<CartDetails> getDishIds(int userId) {
        Session session = null;
        Transaction tx = null;
        List<CartDetails> cartDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<CartDetails> query = session.createQuery("FROM CartDetails WHERE userId = :USER_ID", CartDetails.class);
            query.setParameter("USER_ID", userId);
            cartDetailsList = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return cartDetailsList;
    }

    @Override
    public boolean deleteItem(int userId, int dishId) {
        Session session = null;
        Transaction tx = null;
        boolean success = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query query = session.createQuery("DELETE FROM CartDetails WHERE userId = :userId AND dishId = :dishId");
            query.setParameter("userId", userId);
            query.setParameter("dishId", dishId);
            int rowsAffected = query.executeUpdate();
            success = rowsAffected > 0; // If any row was deleted, consider it a success
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace(); // Print or handle the exception appropriately
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return success;
    }

    @Override
    public boolean deleteAllItem(int userId) {
        Session session = null;
        Transaction tx = null;
        boolean success = false;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query query = session.createQuery("DELETE  FROM CartDetails WHERE userId = :userId");
            query.setParameter("userId", userId);
            int rowsAffected = query.executeUpdate();
            success = rowsAffected > 0; // If any row was deleted, consider it a success
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace(); // Print or handle the exception appropriately
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return success;
    }

}
