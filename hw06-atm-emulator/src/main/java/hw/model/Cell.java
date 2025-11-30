package hw.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
 * Класс реализует логику ячейки банкомата - хранилище купюр одинакового номинала
 * */
@AllArgsConstructor
public class Cell {

    @Getter
    private final Banknote banknote;

    @Getter
    private int count;

    public Cell(Banknote banknote) {
        this.banknote = banknote;
        this.count = 0;
    }

    public void depositMoney(int banknoteCount) {
        this.count += banknoteCount;
    }

    public void withDrawMoney(int banknoteCount) {
        if (banknoteCount > this.count) {
            throw new IllegalArgumentException("Not enough banknotes in this cell");
        }
        this.count -= banknoteCount;
    }

    public int getTotalAmount() {
        return banknote.getNominalValue() * count;
    }
}
