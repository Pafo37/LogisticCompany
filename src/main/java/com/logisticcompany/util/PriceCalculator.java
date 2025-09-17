package com.logisticcompany.util;

public class PriceCalculator {

    public static double calculatePrice(double weight, boolean isDeliveredToOffice) {
        double basePrice = 5.0;
        double weightFactor = weight * 0.5;

        if (isDeliveredToOffice) {
            return basePrice + weightFactor;
        } else {
            return (basePrice + weightFactor) * 1.5;
        }
    }
}
