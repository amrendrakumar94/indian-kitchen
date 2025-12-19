package com.example.kitchen.daoImplementation;

import com.example.kitchen.dao.DishDao;
import com.example.kitchen.dto.FilterDto;
import com.example.kitchen.modal.DishDetails;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class DishDaoImplementation implements DishDao {

    private final SessionFactory sessionFactory;

    @Override
    public List<DishDetails> getDishDetailsList(String cuisineType) {
        Session session = null;
        Transaction tx = null;
        List<DishDetails> dishDetailsList = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Query<DishDetails> query = session.createQuery("FROM DishDetails WHERE cuisineType = :CUISINE_TYPE",
                    DishDetails.class);
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
            List<Integer> dishIdList = Arrays.stream(dishIds.split(",")).map(String::trim).map(Integer::valueOf)
                    .collect(Collectors.toList());

            // Create and execute the query
            Query<DishDetails> query = session.createQuery("FROM DishDetails WHERE id IN (:dishIds)",
                    DishDetails.class);
            query.setParameterList("dishIds", dishIdList);
            dishDetailsList = query.getResultList();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace(); // Consider using a logging framework
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return dishDetailsList;
    }

    @Override
    public List<DishDetails> searchProducts(FilterDto filters, String sortBy, String sortOrder, int page,
            int pageSize) {
        Session session = null;
        Transaction tx = null;
        List<DishDetails> dishDetailsList = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            StringBuilder hql = new StringBuilder("FROM DishDetails WHERE 1=1");

            // Apply filters
            if (filters != null) {
                if (filters.getCategory() != null && !filters.getCategory().isEmpty()) {
                    hql.append(" AND category = :category");
                }
                if (filters.getMinPrice() != null) {
                    hql.append(" AND price >= :minPrice");
                }
                if (filters.getMaxPrice() != null) {
                    hql.append(" AND price <= :maxPrice");
                }
                if (filters.getSpiceLevel() != null) {
                    hql.append(" AND spicinessLevel = :spiceLevel");
                }
                if (filters.getDietary() != null && !filters.getDietary().isEmpty()) {
                    // Check if vegetarian is in the list
                    if (filters.getDietary().stream().anyMatch(d -> "vegetarian".equalsIgnoreCase(d.toString()))) {
                        hql.append(" AND isVegetarian = true");
                    }
                }
                if (filters.getSearch() != null && !filters.getSearch().isEmpty()) {
                    hql.append(
                            " AND (LOWER(name) LIKE :search OR LOWER(description) LIKE :search OR LOWER(ingredientsList) LIKE :search)");
                }
                if (filters.getCuisine() != null && !filters.getCuisine().isEmpty()) {
                    hql.append(" AND cuisineType = :cuisine");
                }
                if (filters.getMealType() != null && !filters.getMealType().isEmpty()) {
                    hql.append(" AND mealType = :mealType");
                }
            }

            // Apply sorting
            if (sortBy != null && !sortBy.isEmpty()) {
                switch (sortBy.toLowerCase()) {
                    case "name":
                        hql.append(" ORDER BY name");
                        break;
                    case "price":
                        hql.append(" ORDER BY price");
                        break;
                    case "rating":
                        hql.append(" ORDER BY rating");
                        break;
                    case "popular":
                    default:
                        hql.append(" ORDER BY orderCount");
                        break;
                }

                if (sortOrder != null && sortOrder.equalsIgnoreCase("asc")) {
                    hql.append(" ASC");
                } else {
                    hql.append(" DESC");
                }
            }

            Query<DishDetails> query = session.createQuery(hql.toString(), DishDetails.class);

            // Set filter parameters
            if (filters != null) {
                if (filters.getCategory() != null && !filters.getCategory().isEmpty()) {
                    query.setParameter("category", filters.getCategory());
                }
                if (filters.getMinPrice() != null) {
                    query.setParameter("minPrice", filters.getMinPrice());
                }
                if (filters.getMaxPrice() != null) {
                    query.setParameter("maxPrice", filters.getMaxPrice());
                }
                if (filters.getSpiceLevel() != null) {
                    // Convert enum to int (assuming mild=1, medium=2, hot=3, extraHot=4)
                    int spiceValue = getSpiceLevelValue(filters.getSpiceLevel().toString());
                    query.setParameter("spiceLevel", spiceValue);
                }
                if (filters.getSearch() != null && !filters.getSearch().isEmpty()) {
                    query.setParameter("search", "%" + filters.getSearch().toLowerCase() + "%");
                }
                if (filters.getCuisine() != null && !filters.getCuisine().isEmpty()) {
                    query.setParameter("cuisine", filters.getCuisine());
                }
                if (filters.getMealType() != null && !filters.getMealType().isEmpty()) {
                    query.setParameter("mealType", filters.getMealType());
                }
            }

            // Apply pagination
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            dishDetailsList = query.getResultList();
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
        return dishDetailsList;
    }

    @Override
    public Long countProducts(FilterDto filters) {
        Session session = null;
        Transaction tx = null;
        Long count = 0L;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            StringBuilder hql = new StringBuilder("SELECT COUNT(*) FROM DishDetails WHERE 1=1");

            // Apply same filters as searchProducts
            if (filters != null) {
                if (filters.getCategory() != null && !filters.getCategory().isEmpty()) {
                    hql.append(" AND category = :category");
                }
                if (filters.getMinPrice() != null) {
                    hql.append(" AND price >= :minPrice");
                }
                if (filters.getMaxPrice() != null) {
                    hql.append(" AND price <= :maxPrice");
                }
                if (filters.getSpiceLevel() != null) {
                    hql.append(" AND spicinessLevel = :spiceLevel");
                }
                if (filters.getDietary() != null && !filters.getDietary().isEmpty()) {
                    if (filters.getDietary().stream().anyMatch(d -> "vegetarian".equalsIgnoreCase(d.toString()))) {
                        hql.append(" AND isVegetarian = true");
                    }
                }
                if (filters.getSearch() != null && !filters.getSearch().isEmpty()) {
                    hql.append(
                            " AND (LOWER(name) LIKE :search OR LOWER(description) LIKE :search OR LOWER(ingredientsList) LIKE :search)");
                }
                if (filters.getCuisine() != null && !filters.getCuisine().isEmpty()) {
                    hql.append(" AND cuisineType = :cuisine");
                }
                if (filters.getMealType() != null && !filters.getMealType().isEmpty()) {
                    hql.append(" AND mealType = :mealType");
                }
            }

            Query<Long> query = session.createQuery(hql.toString(), Long.class);

            // Set filter parameters
            if (filters != null) {
                if (filters.getCategory() != null && !filters.getCategory().isEmpty()) {
                    query.setParameter("category", filters.getCategory());
                }
                if (filters.getMinPrice() != null) {
                    query.setParameter("minPrice", filters.getMinPrice());
                }
                if (filters.getMaxPrice() != null) {
                    query.setParameter("maxPrice", filters.getMaxPrice());
                }
                if (filters.getSpiceLevel() != null) {
                    int spiceValue = getSpiceLevelValue(filters.getSpiceLevel().toString());
                    query.setParameter("spiceLevel", spiceValue);
                }
                if (filters.getSearch() != null && !filters.getSearch().isEmpty()) {
                    query.setParameter("search", "%" + filters.getSearch().toLowerCase() + "%");
                }
                if (filters.getCuisine() != null && !filters.getCuisine().isEmpty()) {
                    query.setParameter("cuisine", filters.getCuisine());
                }
                if (filters.getMealType() != null && !filters.getMealType().isEmpty()) {
                    query.setParameter("mealType", filters.getMealType());
                }
            }

            count = query.getSingleResult();
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
        return count;
    }

    private int getSpiceLevelValue(String spiceLevel) {
        switch (spiceLevel.toLowerCase()) {
            case "mild":
                return 1;
            case "medium":
                return 2;
            case "hot":
                return 3;
            case "extrahot":
                return 4;
            default:
                return 2; // default to medium
        }
    }

}
