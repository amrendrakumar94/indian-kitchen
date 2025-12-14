package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.ReservationDao;
import com.example.kitchen.modal.ReservationDetails;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class ReservationDaoImplementation implements ReservationDao {
    SessionFactory sessionFactory;

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
            log.error("error in getReservationDetailsByUserId()!", e);
        } finally {
            if (tx != null) {
                tx.rollback();
                session.close();
            }
        }
        return reservationDetailsList;
    }
}
