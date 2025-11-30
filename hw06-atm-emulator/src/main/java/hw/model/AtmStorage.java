package hw.model;

import java.util.*;

public class AtmStorage {
    private final Map<Banknote, Cell> cells =
            new TreeMap<>(Comparator.comparingInt(Banknote::getNominalValue).reversed());

    public AtmStorage() {
        for (Banknote b : Banknote.values()) {
            cells.put(b, new Cell(b));
        }
    }

    public Cell getCell(Banknote banknote) {
        return cells.get(banknote);
    }

    public Collection<Cell> allCells() {
        return cells.values();
    }

    public int getBalance() {
        return cells.values().stream().mapToInt(Cell::getTotalAmount).sum();
    }
}
