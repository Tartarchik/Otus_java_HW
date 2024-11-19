package homework;


import java.util.LinkedList;

public class CustomerReverseOrder {
    private LinkedList<Customer> linkedListCustomer = new LinkedList<>();

    public void add(Customer customer) {
        linkedListCustomer.add(customer);
    }

    public Customer take() {
        return linkedListCustomer.removeLast();
    }
}
