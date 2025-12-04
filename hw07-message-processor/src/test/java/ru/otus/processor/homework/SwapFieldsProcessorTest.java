package ru.otus.processor.homework;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

class SwapFieldsProcessorTest {

    @DisplayName("Проверяет, что произошел обмен значений полей Field1 <-> Field12")
    @Test
    void process() {
        Message msg =
                new Message.Builder(1L).field1("Field1").field12("Field12").build();
        Processor processor = new SwapFieldsProcessor();
        Message result = processor.process(msg);

        assertEquals(msg.getField1(), result.getField12());
        assertEquals(msg.getField12(), result.getField1());
    }
}
