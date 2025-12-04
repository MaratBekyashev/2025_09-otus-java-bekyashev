package ru.otus;

import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.Listener;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.SwapFieldsProcessor;
import ru.otus.processor.homework.ThrowExceptionInEvenSecondProcessor;

@Slf4j
public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение
        */
        List<Processor> processors =
                List.of(new SwapFieldsProcessor(), new ThrowExceptionInEvenSecondProcessor(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {});

        Listener listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .field12("field12")
                .build();

        var result = complexProcessor.handle(message);
        log.info("result:{}", result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
