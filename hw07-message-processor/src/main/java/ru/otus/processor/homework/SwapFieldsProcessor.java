package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class SwapFieldsProcessor implements Processor {
    // todo: 2. Сделать процессор, который поменяет местами значения field11 и field12
    @Override
    public Message process(Message message) {
        var result = message.toBuilder()
                .field1(message.getField12())
                .field12(message.getField1())
                .build();

        return result;
    }
}
