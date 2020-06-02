package pl.edu.agh.internetshop;

import java.math.BigDecimal;

public class Product {
	
	public static final int PRICE_PRECISION = 2;
	public static final int ROUND_STRATEGY = BigDecimal.ROUND_HALF_UP;
	
    private final String name;
    private final BigDecimal price;
    private BigDecimal discountValue = BigDecimal.valueOf(1);

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.price.setScale(PRICE_PRECISION, ROUND_STRATEGY);
    }

    public void setDiscountValue(double discountValue) {
        if(discountValue>1 || discountValue<0) throw new IllegalArgumentException("Value of discount out of range (0,1)");
        this.discountValue = BigDecimal.valueOf(1-discountValue);
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public BigDecimal getPriceWithDiscount() {
        return getPrice().multiply(discountValue).setScale(Product.PRICE_PRECISION, Product.ROUND_STRATEGY);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
