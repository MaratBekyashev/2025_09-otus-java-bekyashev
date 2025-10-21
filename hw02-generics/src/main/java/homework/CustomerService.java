package homework;

import java.util.*;

public class CustomerService {

    TreeMap<Customer, String> customersMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customersMap.firstEntry();
        if (entry == null) {
            return null;
        }
        var originalKey = entry.getKey();
        var copy = new Customer(originalKey.getId(), originalKey.getName(), originalKey.getScores());
        return Map.entry(copy, entry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customersMap.higherEntry(customer);
        if (entry == null) {
            return null;
        }
        var originalKey = entry.getKey();
        var copy = new Customer(originalKey.getId(), originalKey.getName(), originalKey.getScores());
        return Map.entry(copy, entry.getValue());
    }

    public void add(Customer customer, String data) {
        customersMap.put(customer, data);
    }
}
