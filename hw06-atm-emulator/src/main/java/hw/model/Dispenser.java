package hw.model;

import java.util.HashMap;
import java.util.Map;

// Класс реализует снятие требуемой суммы с учетом имеющихся запасов купюр
public class Dispenser {

    public Map<Banknote, Integer> withdrawMoney(AtmStorage storage, int sum) {
        int remaining = sum;
        Map<Banknote, Integer> plan = new HashMap<>();

        // Определяем план выдачи : номиналы и количество купюр
        for (Cell cell : storage.allCells()) {
            if (remaining <= 0) {
                break;
            }

            int banknoteValue = cell.getBanknote().getNominalValue();
            int need = remaining / banknoteValue;
            int canGive = Math.min(need, cell.getCount());

            if (canGive > 0) {
                plan.put(cell.getBanknote(), canGive);
                remaining -= canGive * banknoteValue;
            }
        }

        if (remaining > 0) {
            throw new IllegalArgumentException("Its unable to dithDraw needed amount:   " + sum);
        }

        // вынуть деньги из ячеек в соответствии с планом выдачи
        plan.forEach((banknote, count) -> storage.getCell(banknote).withDrawMoney(count));

        return plan;
    }
}
