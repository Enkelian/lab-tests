package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Order;
import pl.edu.agh.internetshop.Product;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;



public class ProductNameSearchStrategyTest {

    @Test
    public void testSameProductName(){
        //given
        String productName = "product name";
        String searchedName = "product name";

        Order order = mock(Order.class);
        Product product = mock(Product.class);
        given(product.getName()).willReturn(productName);

        given(order.getProducts()).willReturn(Collections.singletonList(product));
        ProductNameSearchStrategy productNameSearchStrategy = new ProductNameSearchStrategy(searchedName);


        //when


        //then

        assertTrue(productNameSearchStrategy.filter(order));

    }

    @Test
    public void testDifferentProductName(){
        //given
        String productName = "product name";
        String searchedName = "searched name";

        Order order = mock(Order.class);
        Product product = mock(Product.class);
        given(product.getName()).willReturn(productName);
        given(order.getProducts()).willReturn(Collections.singletonList(product));

        //when
        ProductNameSearchStrategy productNameSearchStrategy = new ProductNameSearchStrategy(searchedName);

        //then

        assertFalse(productNameSearchStrategy.filter(order));
    }
}
