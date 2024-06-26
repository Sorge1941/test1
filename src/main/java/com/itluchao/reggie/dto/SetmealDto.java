package com.itluchao.reggie.dto;

import com.itluchao.reggie.entity.Setmeal;
import com.itluchao.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
