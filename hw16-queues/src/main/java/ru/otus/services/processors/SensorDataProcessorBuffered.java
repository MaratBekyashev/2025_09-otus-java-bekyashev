package ru.otus.services.processors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;

    private final List<SensorData> buffer = new ArrayList<>();
    private final Object lock = new Object();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
    }

    @Override
    public void process(SensorData data) {
        synchronized (lock) {
            buffer.add(data);
            if (buffer.size() >= bufferSize) {
                flush();
            }
        }
    }

    public void flush() {
        List<SensorData> dataToWrite;

        synchronized (lock) {
            if (buffer.isEmpty()) {
                return;
            }

            dataToWrite = new ArrayList<>(buffer);
            buffer.clear();
        }

        dataToWrite.sort(Comparator.comparing(SensorData::getMeasurementTime));

        try {
            writer.writeBufferedData(dataToWrite);
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
