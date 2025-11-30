package hw.model;

import java.util.Map;

public class Atm {

    private final AtmStorage storage;

    private final Dispenser dispenser;

    public static Atm createEmptyAtm() {
        return new Atm();
    }

    private Atm() {
        this.storage = new AtmStorage();
        this.dispenser = new Dispenser();
    }

    public void deposit(Banknote banknote, int count) {
        if (count > 0) {
            storage.getCell(banknote).depositMoney(count);
        }
    }

    public Map<Banknote, Integer> withdraw(int amount) {
        return dispenser.withdrawMoney(storage, amount);
    }

    public int getBalance() {
        return storage.getBalance();
    }
}
