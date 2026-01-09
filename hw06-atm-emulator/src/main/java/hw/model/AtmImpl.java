package hw.model;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtmImpl implements Atm {

    private final AtmStorage storage;

    private final Dispenser dispenser;

    @Override
    public void deposit(Banknote banknote, int count) {
        if (count > 0) {
            storage.getCell(banknote).depositMoney(count);
        }
    }

    @Override
    public Map<Banknote, Integer> withdraw(int amount) {
        return dispenser.withdrawMoney(storage, amount);
    }

    @Override
    public int getBalance() {
        return storage.getBalance();
    }
}
