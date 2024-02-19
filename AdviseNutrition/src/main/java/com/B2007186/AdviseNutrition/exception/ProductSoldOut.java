package com.B2007186.AdviseNutrition.exception;

public class ProductSoldOut extends RuntimeException{
    public ProductSoldOut() {
        super();
    }

    public ProductSoldOut(String message) {
        super(message);
    }

    public ProductSoldOut(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductSoldOut(Throwable cause) {
        super(cause);
    }

    protected ProductSoldOut(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
