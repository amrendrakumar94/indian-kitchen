package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.DishDao;
import com.example.kitchen.modal.DishDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DishDaoImplementation implements DishDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<DishDetails> getDishDetailsList(String cuisineType) {
        Session session = null;
        Transaction tx = null;
        List<DishDetails> dishDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<DishDetails> query = session.createQuery("FROM DishDetails WHERE cuisineType = :CUISINE_TYPE", DishDetails.class);
            query.setParameter("CUISINE_TYPE", cuisineType);
            dishDetailsList = query.getResultList();
            tx.commit();
            return dishDetailsList;
        } catch (Error e) {
            if (tx != null) {
                tx.rollback();
            }
            return dishDetailsList;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<DishDetails> getAllDishes() {
        Session session = null;
        Transaction tx = null;
        List<DishDetails> dishDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<DishDetails> query = session.createQuery("From DishDetails", DishDetails.class);
            dishDetailsList = query.getResultList();
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
        return dishDetailsList;
    }

    @Override
    public DishDetails getDishById(int dishId) {
        Session session = null;
        Transaction tx = null;
        DishDetails dishDetails = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<DishDetails> query = session.createQuery("FROM DishDetails WHERE id = :ID", DishDetails.class);
            query.setParameter("ID", dishId);
            dishDetails = query.getSingleResult();
            tx.commit();
            return dishDetails;
        } catch (Error e) {
            if (tx != null) {
                tx.rollback();
            }
            return dishDetails;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public void save(DishDetails dishDetails) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(dishDetails);
            tx.commit();
        } catch (Error e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<DishDetails> getDishDetailsByDishIds(String dishIds) {
        Session session = null;
        Transaction tx = null;
        List<DishDetails> dishDetailsList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            // Convert comma-separated string to a list of integers
            List<Integer> dishIdList = Arrays.stream(dishIds.split(",")).map(String::trim).map(Integer::valueOf).collect(Collectors.toList());

            // Create and execute the query
            Query<DishDetails> query = session.createQuery("FROM DishDetails WHERE id IN (:dishIds)", DishDetails.class);
            query.setParameterList("dishIds", dishIdList);
            dishDetailsList = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();  // Consider using a logging framework
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return dishDetailsList;
    }

}
