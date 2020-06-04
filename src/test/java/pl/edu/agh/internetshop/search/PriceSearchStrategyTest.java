package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Order;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;



public class PriceSearchStrategyTest {

    private static final BigDecimal orderPrice = BigDecimal.valueOf(90);

    @Test
    public void testSamePrice(){
        //given
        BigDecimal searchedPrice = BigDecimal.valueOf(90);
        Order order = mock(Order.class);

        given(order.getPrice()).willReturn(orderPrice);

        PriceSearchStrategy priceSearchStrategy = new PriceSearchStrategy(searchedPrice);


        //when


        //then
        assertTrue(priceSearchStrategy.filter(order));
    }

    @Test
    public void testDifferentPrice(){
        //given
        BigDecimal searchedPrice = BigDecimal.valueOf(70);
        Order order = mock(Order.class);

        given(order.getPrice()).willReturn(orderPrice);


        //when

        PriceSearchStrategy priceSearchStrategy = new PriceSearchStrategy(searchedPrice);


        //then
        assertFalse(priceSearchStrategy.filter(order));
    }
}
