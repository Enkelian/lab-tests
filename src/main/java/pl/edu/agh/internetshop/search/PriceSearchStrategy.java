package pl.edu.agh.internetshop.search;

import pl.edu.agh.internetshop.Order;

import java.math.BigDecimal;

public class PriceSearchStrategy implements SearchStrategy{

    private BigDecimal price;

    public PriceSearchStrategy(BigDecimal price){
        this.price = price;
    }

    @Override
    public boolean filter(Order order) {
        return this.price.equals(order.getPrice());
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
