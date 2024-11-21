package homework;


import java.util.Deque;
import java.util.LinkedList;

public class CustomerReverseOrder {
    private Deque<Customer> linkedListCustomer = new LinkedList<>();

    public void add(Customer customer) {
        linkedListCustomer.add(customer);
    }

    public Customer take() {
        return linkedListCustomer.removeLast();
    }
}
