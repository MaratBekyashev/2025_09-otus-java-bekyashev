package ru.otus.api.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SensorData {
    private final LocalDateTime measurementTime;
    private final String room;
    private final Double value;

    @Override
    public String toString() {
        return "SensorData{" + "measurementTime="
                + measurementTime + ", room='"
                + room + '\'' + ", value="
                + value + '}';
    }
}
