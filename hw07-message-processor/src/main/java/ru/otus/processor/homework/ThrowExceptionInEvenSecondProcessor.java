package ru.otus.processor.homework;

import lombok.RequiredArgsConstructor;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

// todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с
// гарантированным результатом)
// Секунда должна определяться во время выполнения.
@RequiredArgsConstructor
public class ThrowExceptionInEvenSecondProcessor implements Processor {

    private final TimeProvider timeProvider;

    @Override
    public Message process(Message message) {

        int second = timeProvider.getTime().getSecond();
        if (second % 2 == 0) {
            throw new EvenSecondException("The even second has detected: " + second);
        }

        return message;
    }
}
