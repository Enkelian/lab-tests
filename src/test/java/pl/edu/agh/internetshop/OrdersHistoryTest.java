package pl.edu.agh.internetshop;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.search.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;

public class OrdersHistoryTest {

    private static final BigDecimal searchedPrice = BigDecimal.valueOf(60);
    private static final String searchedProductName = "product1 name";
    private static final String searchedCustomerName = "customer1 name";


    public Order mockOrder(BigDecimal orderPrice, String productName, String customerName){
        Order order = mock(Order.class);

        given(order.getPrice()).willReturn(orderPrice);

        Address address = mock(Address.class);
        given(address.getName()).willReturn(customerName);

        Shipment shipment = mock(Shipment.class);
        given(shipment.getRecipientAddress()).willReturn(address);
        given(order.getShipment()).willReturn(shipment);

        Product product = mock(Product.class);
        given(product.getName()).willReturn(productName);
        given(order.getProducts()).willReturn(Collections.singletonList(product));

        return order;

    }

    @Test
    public void testSearchOnEmptyOrdersHistory(){

        //given
        OrdersHistory ordersHistory = OrdersHistory.getInstance();
        ordersHistory.emptyOrdersList();

        CompositeSearchStrategy compositeSearchStrategy = new CompositeSearchStrategy();
        compositeSearchStrategy.addStrategy(new PriceSearchStrategy(searchedPrice));

        //when
        List<Order> filteredOrders = ordersHistory.getFilteredOrders(compositeSearchStrategy);

        //then
        assertTrue(filteredOrders.isEmpty());
    }

    @Test
    public void testAddOrder(){

        //given
        OrdersHistory ordersHistory = OrdersHistory.getInstance();
        Order order1 = mock(Order.class);

        //when
        ordersHistory.addOrder(order1);


        //then
        assertTrue(ordersHistory.getOrdersList().contains(order1));
    }

    @Test
    public void testGetFilteredOrders(){

        //given

        OrdersHistory ordersHistory = OrdersHistory.getInstance();

        CompositeSearchStrategy compositeSearchStrategy = new CompositeSearchStrategy();
        compositeSearchStrategy.addStrategy(new PriceSearchStrategy(searchedPrice));
        compositeSearchStrategy.addStrategy(new ProductNameSearchStrategy(searchedProductName));
        compositeSearchStrategy.addStrategy(new CustomerNameSearchStrategy(searchedCustomerName));


        Order order1 = mockOrder(searchedPrice, searchedProductName, searchedCustomerName);
        Order order2 = mockOrder(searchedPrice, searchedProductName, "antyhing else");

        ordersHistory.addOrder(order1);
        ordersHistory.addOrder(order2);

        //when
        List<Order> filteredOrders = ordersHistory.getFilteredOrders(compositeSearchStrategy);

        //then
        assertNotNull(filteredOrders);
        assertEquals(Collections.singletonList(order1), filteredOrders);
        assertSame(order1, filteredOrders.get(0));

    }



    @Test
    public void testSearchWithEmptySearchStrategy(){

        //given

        OrdersHistory ordersHistory = OrdersHistory.getInstance();
        Order order1 = mockOrder(searchedPrice, searchedProductName, searchedCustomerName);

        ordersHistory.addOrder(order1);

        CompositeSearchStrategy compositeSearchStrategy = new CompositeSearchStrategy();

        //when
        List<Order> filteredOrders = ordersHistory.getFilteredOrders(compositeSearchStrategy);

        //then
       assertEquals(ordersHistory.getOrdersList(), filteredOrders);

    }


}
