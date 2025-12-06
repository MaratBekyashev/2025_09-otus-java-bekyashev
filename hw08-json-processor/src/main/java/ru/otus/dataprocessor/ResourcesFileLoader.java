package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final InputStream inputStream;

    private final ObjectMapper mapper;

    public ResourcesFileLoader(String fileName) {
        this.inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if (this.inputStream == null) {
            throw new RuntimeException("File resource not found: %s".formatted(fileName));
        }
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        try {
            var result = mapper.readValue(this.inputStream, new TypeReference<List<Measurement>>() {});
            return result;
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
