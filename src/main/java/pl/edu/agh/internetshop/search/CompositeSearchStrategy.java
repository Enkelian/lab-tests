package pl.edu.agh.internetshop.search;

import pl.edu.agh.internetshop.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeSearchStrategy implements SearchStrategy {

    private List<SearchStrategy> searchStrategies;

    public CompositeSearchStrategy(){
        this.searchStrategies = new ArrayList<>();
    }

    @Override
    public boolean filter(Order order) {
        return this.searchStrategies
                .stream()
                .allMatch(s -> s.filter(order));
    }

    public void addStrategy(SearchStrategy searchStrategy){
        this.searchStrategies.add(searchStrategy);
    }

    public void removeStrategy(SearchStrategy searchStrategy){
        this.searchStrategies.remove(searchStrategy);
    }

    public List<SearchStrategy> getSearchStrategies(){ return this.searchStrategies; }
}
