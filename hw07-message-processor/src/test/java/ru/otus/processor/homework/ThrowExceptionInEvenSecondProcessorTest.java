package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

class ThrowExceptionInEvenSecondProcessorTest {

    @DisplayName("Проверка выброса исключения в четную секунду")
    @Test
    public void CheckEvenSecond() {
        TimeProvider provider = () -> LocalDateTime.of(2025, 12, 3, 12, 34, 20);
        Processor processor = new ThrowExceptionInEvenSecondProcessor(provider);
        assertThrows(EvenSecondException.class, () -> processor.process(null));
    }

    @DisplayName("Проверка корретной обработки в нечетную секунду")
    @Test
    public void CheckNonEvenSecond() {
        TimeProvider provider = () -> LocalDateTime.of(2025, 12, 3, 12, 34, 21);
        Message msg = new Message.Builder(1L).field1("abc").build();
        Processor processor = new ThrowExceptionInEvenSecondProcessor(provider);
        Message result = processor.process(msg);

        assertEquals(msg, result);
    }
}
