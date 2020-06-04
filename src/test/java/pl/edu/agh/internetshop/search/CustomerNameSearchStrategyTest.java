package pl.edu.agh.internetshop.search;

import org.junit.jupiter.api.Test;
import pl.edu.agh.internetshop.Address;
import pl.edu.agh.internetshop.Order;
import pl.edu.agh.internetshop.Shipment;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class CustomerNameSearchStrategyTest {

    private static final String customerName = "customer name";

    @Test
    public void testSameCustomerName(){
        //given
        String searchedName = "customer name";

        Address address = mock(Address.class);
        given(address.getName()).willReturn(customerName);

        Shipment shipment = mock(Shipment.class);
        given(shipment.getRecipientAddress()).willReturn(address);

        Order order = mock(Order.class);
        given(order.getShipment()).willReturn(shipment);

        CustomerNameSearchStrategy customerNameSearchStrategy = new CustomerNameSearchStrategy(searchedName);


        //when


        //then

        assertTrue(customerNameSearchStrategy.filter(order));

    }

    @Test
    public void testDifferentCustomerName(){
        //given
        String searchedName = "searched name";

        Address address = mock(Address.class);
        given(address.getName()).willReturn(customerName);

        Shipment shipment = mock(Shipment.class);
        given(shipment.getRecipientAddress()).willReturn(address);

        Order order = mock(Order.class);
        given(order.getShipment()).willReturn(shipment);

        //when
        CustomerNameSearchStrategy customerNameSearchStrategy = new CustomerNameSearchStrategy(searchedName);


        //then

        assertFalse(customerNameSearchStrategy.filter(order));

    }


}
