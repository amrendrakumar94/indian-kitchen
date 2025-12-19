package com.example.kitchen.service;

import com.example.kitchen.dao.DishDao;
import com.example.kitchen.dao.UserDao;
import com.example.kitchen.dto.*;
import com.example.kitchen.modal.CartDetails;
import com.example.kitchen.modal.DishDetails;
import com.example.kitchen.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DishService {

    private final DishDao dishDao;
    private final UserDao userDao;

    public DishDetailsDto getAllDishes(int userId, String cuisineType) {
        DishDetailsDto dishDetailsDto = new DishDetailsDto();
        try {
            List<DishDetails> dishDetailsList;
            List<CartDetails> cartDetailsList;
            List<Integer> dishIds = new ArrayList<>();
            if (!"null".equalsIgnoreCase(cuisineType) && CommonUtils.isNotNullAndNotEmpty(cuisineType)
                    && !"All".equalsIgnoreCase(cuisineType)) {
                dishDetailsList = dishDao.getDishDetailsList(cuisineType);
                cartDetailsList = userDao.getDishIds(userId);
                if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                    for (CartDetails obj : cartDetailsList) {
                        if (obj != null) {
                            dishIds.add(obj.getDishId());
                        }
                    }
                }
            } else {
                dishDetailsList = dishDao.getAllDishes();
                cartDetailsList = userDao.getDishIds(userId);
                if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                    for (CartDetails obj : cartDetailsList) {
                        if (obj != null) {
                            dishIds.add(obj.getDishId());
                        }
                    }
                }
            }
            dishDetailsDto.setDishDetails(dishDetailsList);
            dishDetailsDto.setDishIds(dishIds);
        } catch (Exception e) {
            log.error("Error in getAllDishes(). ", e);
        }
        return dishDetailsDto;
    }

    public List<DishDetails> getDishDetailsByDishIds(String dishIds) {
        try {
            List<DishDetails> dishDetailsList = dishDao.getDishDetailsByDishIds(dishIds);
            return dishDetailsList;
        } catch (Exception e) {
            log.error("Error in getDishDetailsByDishIds(). ", e);
        }
        return null;
    }

    public ProductSearchResponseDto searchProducts(ProductSearchRequestDto request) {
        ProductSearchResponseDto response = new ProductSearchResponseDto();
        try {
            // Validate and set defaults
            int page = request.getPage() != null && request.getPage() > 0 ? request.getPage() : 1;
            int pageSize = request.getPageSize() != null && request.getPageSize() > 0 && request.getPageSize() <= 100
                    ? request.getPageSize()
                    : 20;
            String sortBy = request.getSortBy() != null && !request.getSortBy().isEmpty()
                    ? request.getSortBy()
                    : "popular";
            String sortOrder = request.getSortOrder() != null && !request.getSortOrder().isEmpty()
                    ? request.getSortOrder()
                    : "desc";
            FilterDto filters = request.getFilters();

            // Get products from DAO
            List<DishDetails> dishDetailsList = dishDao.searchProducts(filters, sortBy, sortOrder, page, pageSize);
            Long totalCount = dishDao.countProducts(filters);

            // Get cart items if userId is provided
            final List<Integer> cartDishIds;
            if (request.getUserId() != null && request.getUserId() > 0) {
                List<CartDetails> cartDetailsList = userDao.getDishIds(request.getUserId());
                if (cartDetailsList != null && !cartDetailsList.isEmpty()) {
                    cartDishIds = cartDetailsList.stream()
                            .filter(Objects::nonNull)
                            .map(CartDetails::getDishId)
                            .collect(Collectors.toList());
                } else {
                    cartDishIds = new ArrayList<>();
                }
            } else {
                cartDishIds = new ArrayList<>();
            }

            // Convert to ProductResponseDto
            List<ProductResponseDto> products = dishDetailsList.stream()
                    .map(dish -> convertToProductResponse(dish, cartDishIds))
                    .collect(Collectors.toList());

            // Build pagination
            PaginationDto pagination = buildPagination(page, pageSize, totalCount);

            // Build applied filters
            AppliedFiltersDto appliedFilters = buildAppliedFilters(filters);

            response.setProducts(products);
            response.setPagination(pagination);
            response.setFilters(appliedFilters);

        } catch (Exception e) {
            log.error("Error in searchProducts(). ", e);
        }
        return response;
    }

    private ProductResponseDto convertToProductResponse(DishDetails dish, List<Integer> cartDishIds) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(dish.getId());
        dto.setName(dish.getName());
        dto.setDescription(dish.getDescription());
        dto.setPrice(dish.getPrice());
        dto.setOriginalPrice(dish.getOriginalPrice());
        dto.setDiscount(dish.getDiscount());
        dto.setCategory(dish.getCategory());
        dto.setImage(dish.getImage());

        // Convert single image to images array
        if (dish.getImage() != null && !dish.getImage().isEmpty()) {
            dto.setImages(Arrays.asList(dish.getImage()));
        }

        dto.setRating(dish.getRating());
        dto.setReviewCount(dish.getReviewCount());
        dto.setInStock(dish.getInStock() != null ? dish.getInStock() : true);
        dto.setServingSize(dish.getServingSize());

        // Convert spiciness level to string
        dto.setSpiceLevel(getSpiceLevelString(dish.getSpicinessLevel()));

        // Build dietary list
        List<String> dietary = new ArrayList<>();
        if (dish.isVegetarian()) {
            dietary.add("vegetarian");
        } else {
            dietary.add("non-vegetarian");
        }
        dto.setDietary(dietary);

        dto.setCuisine(dish.getCuisineType());
        dto.setPreparationTime(dish.getPreparationTime());
        dto.setCalories(dish.getCalories());

        // Convert ingredients string to list
        if (dish.getIngredientsList() != null && !dish.getIngredientsList().isEmpty()) {
            dto.setIngredients(Arrays.asList(dish.getIngredientsList().split(",")));
        }

        // Convert allergens string to list
        if (dish.getAllergens() != null && !dish.getAllergens().isEmpty()) {
            dto.setAllergens(Arrays.asList(dish.getAllergens().split(",")));
        }

        dto.setCustomizable(dish.getCustomizable() != null ? dish.getCustomizable() : false);

        // Add-ons would come from a separate table in production
        dto.setAddOns(new ArrayList<>());

        // Convert tags string to list
        if (dish.getTags() != null && !dish.getTags().isEmpty()) {
            dto.setTags(Arrays.asList(dish.getTags().split(",")));
        }

        dto.setBrand(dish.getBrand());
        dto.setCreatedAt(dish.getCreatedAt());
        dto.setMealType(dish.getMealType());

        return dto;
    }

    private String getSpiceLevelString(int level) {
        switch (level) {
            case 1:
                return "mild";
            case 2:
                return "medium";
            case 3:
                return "hot";
            case 4:
                return "extra-hot";
            default:
                return "medium";
        }
    }

    private PaginationDto buildPagination(int page, int pageSize, Long totalItems) {
        PaginationDto pagination = new PaginationDto();
        pagination.setCurrentPage(page);
        pagination.setPageSize(pageSize);
        pagination.setTotalItems(totalItems);

        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        pagination.setTotalPages(totalPages);
        pagination.setHasNextPage(page < totalPages);
        pagination.setHasPreviousPage(page > 1);

        return pagination;
    }

    private AppliedFiltersDto buildAppliedFilters(FilterDto filters) {
        AppliedFiltersDto appliedFilters = new AppliedFiltersDto();

        // Build applied filters map
        Map<String, Object> appliedFiltersMap = new HashMap<>();
        if (filters != null) {
            if (filters.getCategory() != null && !filters.getCategory().isEmpty()) {
                appliedFiltersMap.put("category", filters.getCategory());
            }
            if (filters.getMinPrice() != null || filters.getMaxPrice() != null) {
                Map<String, Integer> priceRange = new HashMap<>();
                if (filters.getMinPrice() != null)
                    priceRange.put("min", filters.getMinPrice());
                if (filters.getMaxPrice() != null)
                    priceRange.put("max", filters.getMaxPrice());
                appliedFiltersMap.put("priceRange", priceRange);
            }
            if (filters.getSpiceLevel() != null) {
                appliedFiltersMap.put("spiceLevel", filters.getSpiceLevel().toString());
            }
            if (filters.getDietary() != null && !filters.getDietary().isEmpty()) {
                appliedFiltersMap.put("dietary", filters.getDietary());
            }
            if (filters.getSearch() != null && !filters.getSearch().isEmpty()) {
                appliedFiltersMap.put("search", filters.getSearch());
            }
            if (filters.getCuisine() != null && !filters.getCuisine().isEmpty()) {
                appliedFiltersMap.put("cuisine", filters.getCuisine());
            }
            if (filters.getMealType() != null && !filters.getMealType().isEmpty()) {
                appliedFiltersMap.put("mealType", filters.getMealType());
            }
        }
        appliedFilters.setAppliedFilters(appliedFiltersMap);

        // Available categories
        List<String> availableCategories = Arrays.asList(
                "Starters", "Main Course", "Rice & Biryani", "Breads",
                "South Indian", "North Indian", "Desserts", "Beverages", "Combos");
        appliedFilters.setAvailableCategories(availableCategories);

        return appliedFilters;
    }
}
