package homework;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Customer {

    private long id;

    private String name;

    private long scores;

    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name='" + name + '\'' + ", scores=" + scores + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer otherCustomer = (Customer) o;

        return Objects.equals(this.id, otherCustomer.id);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(this.id);
        return result;
    }
}
