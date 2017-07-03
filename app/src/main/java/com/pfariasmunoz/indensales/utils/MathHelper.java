package com.pfariasmunoz.indensales.utils;

import com.pfariasmunoz.indensales.data.models.Article;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by Pablo Farias on 26-04-17.
 */

public class MathHelper {


    public static String setTotalPrice(int amount, String price) {
        BigDecimal decimalAmount = new BigDecimal(amount);
        BigDecimal decimalPrice = new BigDecimal(price);
        BigDecimal totalPrice = decimalAmount.multiply(decimalPrice);
        return totalPrice.setScale(2, RoundingMode.CEILING).toString();
    }

    public static boolean isNumeric(String inputData) {
        return inputData.matches("[-+]?\\d+(\\.\\d+)?");


    }

    public static String getLocalCurrency(String number) {
        String result = "";
        Long n = 0L;
        if (MathHelper.isNumeric(number)) {
            n = Long.valueOf(number);
        }
        NumberFormat format = NumberFormat.getInstance();

        result = String.valueOf(format.format(n)).trim();
        if (result.length() > 4) {
            return result;
        } else {
            if (result.substring(0, 1).equals(".") || result.substring(0, 1).equals(",")) {
                return result.substring(1);
            }
            return result;

        }

    }

    public static BigDecimal getTotalPriceWithDeiscount(Article article, int amount) {
        Double doubleAmount = Double.valueOf(amount);
        Double precio = Double.valueOf(article.getPrecio());
        Double discount1 = Double.valueOf(article.getDescuento1());
        Double discount2 = Double.valueOf(article.getDescuento2());
        Double discount3 = Double.valueOf(article.getDescuento3());
        BigDecimal d1 = BigDecimal.valueOf(precio)
                .multiply(BigDecimal.valueOf(discount1))
                .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_UNNECESSARY);
        BigDecimal d2 = d1.multiply(BigDecimal.valueOf(discount2))
                .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_UNNECESSARY);
        BigDecimal d3 = d2.multiply(BigDecimal.valueOf(discount3))
                .divide(BigDecimal.valueOf(100.0), BigDecimal.ROUND_UNNECESSARY);
        return BigDecimal.valueOf(precio).subtract(d1.add(d2).add(d3)).multiply(BigDecimal.valueOf(doubleAmount));

    }

}
