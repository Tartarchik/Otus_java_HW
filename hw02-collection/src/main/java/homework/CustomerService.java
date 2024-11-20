package homework;

import java.util.*;

public class CustomerService {

    private NavigableMap<Customer, String> mapCustomer = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return copyCustomerEntry(mapCustomer.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyCustomerEntry(mapCustomer.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        mapCustomer.put(customer, data);
    }

    private Map.Entry<Customer, String> copyCustomerEntry(Map.Entry<Customer, String> customerEntry) {
        if (customerEntry != null) {
            Customer customerMap = customerEntry.getKey();
            Customer customer = new Customer(customerMap.getId(), customerMap.getName(),
                    customerMap.getScores());
            return Map.entry(customer, mapCustomer.get(customerMap));
        }
        return null;
    }
}

