package com.example.kitchen.dto;

import com.example.kitchen.modal.DishDetails;
import lombok.Data;

import java.util.List;

@Data
public class DishDetailsDto {
    List<Integer> dishIds;
    List<DishDetails> dishDetails;
}
