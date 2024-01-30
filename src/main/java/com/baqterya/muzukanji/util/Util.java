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

    static public boolean isRomaji(String input) {
        boolean isRomaji = true;
        for (char c : input.toCharArray()) {
            if (Character.UnicodeBlock.of(c) != Character.UnicodeBlock.BASIC_LATIN) {
                isRomaji = false;
                break;
            }
        }
        return isRomaji;
    }

    static public boolean isJapanese(String input) {
        boolean isJapanese = true;
        String inputWithoutPunctuation = input.replaceAll("\\p{P}", "");
        for (char c : inputWithoutPunctuation.toCharArray()) {
            Character.UnicodeBlock block = (Character.UnicodeBlock.of(c));
            if (
                    block != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                            && block != Character.UnicodeBlock.HIRAGANA
                            && block != Character.UnicodeBlock.KATAKANA
                            && block != Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                            && block != Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                            && c != ' '
            ) {
                isJapanese = false;
                break;
            }
        }
        return isJapanese;
    }

    private static Direction getSortingDirection(String[] sort) {
        return sort[1].contains("desc") ? Direction.DESC : Direction.ASC;
    }
}
