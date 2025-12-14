package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.OrderDao;
import com.example.kitchen.modal.OrderedDetails;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class OrderDaoImplementation implements OrderDao {

    SessionFactory sessionFactory;

    @Override
    public List<OrderedDetails> getAllOrderByUserId(int userId) {
        Session session = null;
        Transaction tx = null;
        List<OrderedDetails> orderedDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<OrderedDetails> query = session.createQuery("FROM OrderedDetails WHERE userId =: USER_ID", OrderedDetails.class);
            query.setParameter("USER_ID", userId);
            orderedDetailsList = query.getResultList();
        } catch (Exception e) {
            log.error("error in getAllOrderByUserId()!", e);
        } finally {
            if (tx != null) {
                tx.rollback();
                session.close();
            }
        }
        return orderedDetailsList;
    }

    @Override
    public boolean saveOrder(OrderedDetails orderedDetails) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(orderedDetails);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
