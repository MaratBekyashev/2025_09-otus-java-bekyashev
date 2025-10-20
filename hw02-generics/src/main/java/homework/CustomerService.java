package homework;

import java.util.*;

/*record CustomerKey(long id,
                   String name,
                   long score
                   ) {}
*/
public class CustomerService {
  //  TreeMap<CustomerKey, Customer> orderMap = new TreeMap<>(Comparator.comparingLong(CustomerKey::score));
    TreeMap<Customer, String> customersMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));
    //Map<Customer, String> customersMap = new HashMap<>();
    SequencedMap <Customer, String> sm =
    public Map.Entry<Customer, String> getSmallest() {
        Customer customer = orderMap.firstEntry().getValue();
        // Map.Entry<Customer, String> minEntry = customersMap.firstEntry();
        Map.Entry<Customer, String> result = customersMap.get(customer);
        return minEntry2;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = customersMap.higherEntry(customer);
        return nextEntry;
    }

    public void add(Customer customer, String data) {
        var key = new CustomerKey(customer.getId(), customer.getName(), customer.getScores());
        orderMap.put(key, customer);
        customersMap.put(customer, data);

    }
}
