package pl.edu.agh.internetshop;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import pl.edu.agh.internetshop.search.CompositeSearchStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrdersHistory {

    private static OrdersHistory instance = null;
    private List<Order> ordersList;

    private OrdersHistory(){
        this.ordersList = new ArrayList<>();
    }

    public static OrdersHistory getInstance(){
        if(OrdersHistory.instance == null){
            OrdersHistory.instance = new OrdersHistory();
        }
        return OrdersHistory.instance;
    }

    public void addOrder(Order order){
        this.ordersList.add(order);
    }

    public List<Order> getFilteredOrders(CompositeSearchStrategy searchStrategy){
        return this.ordersList
                .stream()
                .filter(searchStrategy::filter)
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersList(){
        return this.ordersList;
    }

    public void emptyOrdersList(){
        this.ordersList.clear();
    }
}
