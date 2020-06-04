package pl.edu.agh.internetshop.search;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import pl.edu.agh.internetshop.Address;
import pl.edu.agh.internetshop.Order;
import pl.edu.agh.internetshop.Shipment;

public class CustomerNameSearchStrategy implements SearchStrategy {

    private String name;

    public CustomerNameSearchStrategy(String name){
        this.name = name;
    }

    @Override
    public boolean filter(Order order) {

        Shipment shipment = order.getShipment();
        Address address = shipment.getRecipientAddress();

        return this.name.equals(address.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
