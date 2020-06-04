package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Address;
import pl.edu.agh.internetshop.Order;
import pl.edu.agh.internetshop.Product;
import pl.edu.agh.internetshop.Shipment;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CompositeSearchStrategyTest {


    @Test
    public void testAddingStrategy(){

        //given
        CompositeSearchStrategy compositeSearchStrategy = new CompositeSearchStrategy();
        ProductNameSearchStrategy productNameSearchStrategy = mock(ProductNameSearchStrategy.class);
        PriceSearchStrategy priceSearchStrategy = mock(PriceSearchStrategy.class);
        CustomerNameSearchStrategy customerNameSearchStrategy = mock(CustomerNameSearchStrategy.class);
        //when
        compositeSearchStrategy.addStrategy(productNameSearchStrategy);
        compositeSearchStrategy.addStrategy(priceSearchStrategy);

        //then
        assertTrue(compositeSearchStrategy.getSearchStrategies().contains(productNameSearchStrategy));
        assertTrue(compositeSearchStrategy.getSearchStrategies().contains(priceSearchStrategy));
        assertFalse(compositeSearchStrategy.getSearchStrategies().contains(customerNameSearchStrategy));
    }

    @Test
    public void testFilterByAllStrategies(){
        //given
        Order order = mock(Order.class);

        BigDecimal price = BigDecimal.valueOf(80);
        given(order.getPrice()).willReturn(price);

        String productName = "product name";
        Product product = mock(Product.class);
        given(product.getName()).willReturn(productName);
        given(order.getProducts()).willReturn(Collections.singletonList(product));

        String customerName = "customer name";
        Address address = mock(Address.class);
        given(address.getName()).willReturn(customerName);
        Shipment shipment = mock(Shipment.class);
        given(shipment.getRecipientAddress()).willReturn(address);
        given(order.getShipment()).willReturn(shipment);

        PriceSearchStrategy priceSearchStrategy = new PriceSearchStrategy(price);
        ProductNameSearchStrategy productNameSearchStrategy = new ProductNameSearchStrategy(productName);
        CustomerNameSearchStrategy customerNameSearchStrategy = new CustomerNameSearchStrategy(customerName);

        CompositeSearchStrategy compositeSearchStrategy = new CompositeSearchStrategy();

        compositeSearchStrategy.addStrategy(priceSearchStrategy);
        compositeSearchStrategy.addStrategy(productNameSearchStrategy);
        compositeSearchStrategy.addStrategy(customerNameSearchStrategy);

        //when


        //then
        assertTrue(compositeSearchStrategy.filter(order));

    }
}
