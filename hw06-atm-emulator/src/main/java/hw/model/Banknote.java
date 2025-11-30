package hw.model;

import hw.exceptions.BanknoteNotFoundException;
import java.util.Arrays;
import lombok.Getter;

public enum Banknote {
    RUB_10(10),
    RUB_50(50),
    RUB_100(100),
    RUB_500(500),
    RUB_1000(1000),
    RUB_5000(5000);

    @Getter
    private final int nominalValue;

    Banknote(int nominalValue) {
        this.nominalValue = nominalValue;
    }

    public static Banknote findBanknoteByNominal(int nominalValue) {
        Banknote result = Arrays.stream(Banknote.values())
                .filter(i -> i.getNominalValue() == nominalValue)
                .findFirst()
                .orElseThrow(() -> new BanknoteNotFoundException(
                        "Unable to find banknote for your nominal( %d)".formatted(nominalValue)));
        return result;
    }
}
