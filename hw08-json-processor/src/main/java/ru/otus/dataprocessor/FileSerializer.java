package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final OutputStream outStream;

    private final ObjectMapper mapper;

    public FileSerializer(String fileName) {
        try {
            this.outStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.mapper = new ObjectMapper();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try {
            this.mapper.writeValue(outStream, data);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
