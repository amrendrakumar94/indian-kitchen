package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.OrderDao;
import com.example.kitchen.modal.OrderItem;
import com.example.kitchen.modal.OrderedDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OrderDaoImplementation implements OrderDao {

    private final SessionFactory sessionFactory;

    @Override
    public List<OrderedDetails> getAllOrderByUserId(int userId) {
        Session session = null;
        Transaction tx = null;
        List<OrderedDetails> orderedDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<OrderedDetails> query = session.createQuery("FROM OrderedDetails WHERE userId = :USER_ID",
                    OrderedDetails.class);
            query.setParameter("USER_ID", userId);
            orderedDetailsList = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in getAllOrderByUserId()!", e);
        } finally {
            if (session != null && session.isOpen()) {
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
            log.error("error in saveOrder()!", e);
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public boolean save(OrderedDetails order) {
        return saveOrder(order);
    }

    @Override
    public boolean saveOrderItems(List<OrderItem> orderItems) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            for (OrderItem item : orderItems) {
                session.save(item);
            }
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in saveOrderItems()!", e);
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public OrderedDetails findByOrderId(String orderId) {
        Session session = null;
        Transaction tx = null;
        OrderedDetails order = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<OrderedDetails> query = session.createQuery(
                    "FROM OrderedDetails WHERE orderId = :orderId",
                    OrderedDetails.class);
            query.setParameter("orderId", orderId);
            List<OrderedDetails> results = query.getResultList();
            if (results != null && !results.isEmpty()) {
                order = results.get(0);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in findByOrderId()!", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public OrderedDetails findById(int id) {
        Session session = null;
        Transaction tx = null;
        OrderedDetails order = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            order = session.get(OrderedDetails.class, id);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in findById()!", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return order;
    }

    @Override
    public List<OrderedDetails> findAllByUserId(int userId, int page, int pageSize) {
        Session session = null;
        Transaction tx = null;
        List<OrderedDetails> orders = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<OrderedDetails> query = session.createQuery(
                    "FROM OrderedDetails WHERE userId = :userId ORDER BY orderDate DESC",
                    OrderedDetails.class);
            query.setParameter("userId", userId);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            orders = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in findAllByUserId()!", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return orders;
    }

    @Override
    public List<OrderedDetails> findByUserIdAndStatus(int userId, String status, int page, int pageSize) {
        Session session = null;
        Transaction tx = null;
        List<OrderedDetails> orders = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<OrderedDetails> query = session.createQuery(
                    "FROM OrderedDetails WHERE userId = :userId AND status = :status ORDER BY orderDate DESC",
                    OrderedDetails.class);
            query.setParameter("userId", userId);
            query.setParameter("status", status);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            orders = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in findByUserIdAndStatus()!", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return orders;
    }

    @Override
    public long countByUserId(int userId) {
        Session session = null;
        Transaction tx = null;
        long count = 0;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM OrderedDetails WHERE userId = :userId",
                    Long.class);
            query.setParameter("userId", userId);
            count = query.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in countByUserId()!", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return count;
    }

    @Override
    public long countByUserIdAndStatus(int userId, String status) {
        Session session = null;
        Transaction tx = null;
        long count = 0;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM OrderedDetails WHERE userId = :userId AND status = :status",
                    Long.class);
            query.setParameter("userId", userId);
            query.setParameter("status", status);
            count = query.uniqueResult();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in countByUserIdAndStatus()!", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return count;
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(int orderId) {
        Session session = null;
        Transaction tx = null;
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<OrderItem> query = session.createQuery(
                    "FROM OrderItem WHERE orderId = :orderId",
                    OrderItem.class);
            query.setParameter("orderId", orderId);
            orderItems = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in findOrderItemsByOrderId()!", e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return orderItems;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status, String cancellationReason) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            OrderedDetails order = session.get(OrderedDetails.class, orderId);
            if (order != null) {
                order.setStatus(status);
                if (cancellationReason != null) {
                    order.setCancellationReason(cancellationReason);
                }
                session.update(order);
                tx.commit();
                return true;
            }
            tx.commit();
            return false;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error("error in updateOrderStatus()!", e);
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
