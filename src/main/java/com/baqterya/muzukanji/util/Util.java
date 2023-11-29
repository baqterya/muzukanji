package com.baqterya.muzukanji.util;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

public final class Util {
    static public List<Order> getSortingOrder(String[] sort) {
        List<Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Order(getSortingDirection(_sort), _sort[0]));
            }
        } else {
            orders.add(new Order(getSortingDirection(sort), sort[0]));
        }

        return orders;
    }

    private static Direction getSortingDirection(String[] sort) {
        return sort[1].contains("desc") ? Direction.DESC : Direction.ASC;
    }
}
