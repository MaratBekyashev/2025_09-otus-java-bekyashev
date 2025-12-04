package ru.otus.listener.homework;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.HistoryReader;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {
    // todo: 4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
    // Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
    // Для него уже есть тест, убедитесь, что тест проходит
    private Map<Long, Deque<Message>> messageHistory;

    public HistoryListener() {
        messageHistory = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        Message memento = msg.toBuilder().build();
        messageHistory.computeIfAbsent(memento.getId(), k -> new ArrayDeque()).push(memento);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        Deque<Message> history = messageHistory.get(id);
        return Optional.of(history.pop());
    }
}
