package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.CartDao;
import com.example.kitchen.modal.CartDetails;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartDaoImplementation implements CartDao {

    private final SessionFactory sessionFactory;

    @Override
    public CartDetails findByUserIdAndDishId(int userId, int dishId) {
        Session session = null;
        Transaction tx = null;
        CartDetails cartDetails = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query<CartDetails> query = session.createQuery(
                    "FROM CartDetails WHERE userId = :userId AND dishId = :dishId",
                    CartDetails.class);
            query.setParameter("userId", userId);
            query.setParameter("dishId", dishId);

            List<CartDetails> results = query.getResultList();
            if (results != null && !results.isEmpty()) {
                cartDetails = results.get(0);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return cartDetails;
    }

    @Override
    public List<CartDetails> findAllByUserId(int userId) {
        Session session = null;
        Transaction tx = null;
        List<CartDetails> cartDetailsList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query<CartDetails> query = session.createQuery(
                    "FROM CartDetails WHERE userId = :userId",
                    CartDetails.class);
            query.setParameter("userId", userId);
            cartDetailsList = query.getResultList();

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return cartDetailsList;
    }

    @Override
    public boolean save(CartDetails cartDetails) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(cartDetails);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteByUserIdAndDishId(int userId, int dishId) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery(
                    "DELETE FROM CartDetails WHERE userId = :userId AND dishId = :dishId");
            query.setParameter("userId", userId);
            query.setParameter("dishId", dishId);
            query.executeUpdate();

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean deleteAllByUserId(int userId) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery(
                    "DELETE FROM CartDetails WHERE userId = :userId");
            query.setParameter("userId", userId);
            query.executeUpdate();

            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
