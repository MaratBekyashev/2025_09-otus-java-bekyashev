package hw.model;

import java.util.Map;

public interface Atm {

    void deposit(Banknote banknote, int count);

    Map<Banknote, Integer> withdraw(int amount);

    int getBalance();

}
