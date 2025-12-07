package ru.otus.listener;

import lombok.extern.slf4j.Slf4j;
import ru.otus.model.Message;

@Slf4j
public class ListenerPrinterConsole implements Listener {

    @Override
    public void onUpdated(Message msg) {
        log.info("oldMsg:{}", msg);
    }
}
