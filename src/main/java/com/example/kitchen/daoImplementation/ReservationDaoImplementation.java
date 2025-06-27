package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.ReservationDao;
import com.example.kitchen.modal.ReservationDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationDaoImplementation implements ReservationDao {
    @Autowired
    SessionFactory sessionFactory;
    private Logger logger;

    @Override
    public List<ReservationDetails> getReservationDetailsByUserId(int userId) {
        Session session = null;
        Transaction tx = null;
        List<ReservationDetails> reservationDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<ReservationDetails> query = session.createQuery("FROM ReservationDetails WHERE userId=:USER_ID", ReservationDetails.class);
            query.setParameter("USER_ID", userId);
            reservationDetailsList = query.getResultList();
        } catch (Exception e) {
            logger.error("error in getReservationDetailsByUserId()!", e);
        } finally {
            if (tx != null) {
                tx.rollback();
                session.close();
            }
        }
        return reservationDetailsList;
    }
}
