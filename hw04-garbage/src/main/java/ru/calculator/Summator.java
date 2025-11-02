package ru.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;

@Getter
public class Summator {
    @Getter
    private int sum = 0;

    @Getter
    private int prevValue = 0;

    @Getter
    private int prevPrevValue = 0;

    @Getter
    private int sumLastThreeValues = 0;

    @Getter
    private int someValue = 0;

    // !!! эта коллекция должна остаться. Заменять ее на счетчик нельзя.
    private final List<Data> listValues = new ArrayList<>();

    // private final SecureRandom random = new SecureRandom(); // заменен на ThreadLocalRandom

    // !!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        listValues.add(data);
        if (listValues.size() % 100_000 == 0) {
            listValues.clear();
            ((ArrayList) listValues).trimToSize();
        }

        sum += data.getValue() + ThreadLocalRandom.current().nextInt();

        sumLastThreeValues = data.getValue() + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = data.getValue();

        for (int idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (data.getValue() + 1) - sum);
            someValue = Math.abs(someValue) + listValues.size();
        }
    }
}
