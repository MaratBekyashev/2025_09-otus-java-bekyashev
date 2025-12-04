package ru.otus.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectForMessage {

    private List<String> data;

    public ObjectForMessage copy() {
        ObjectForMessage copy = new ObjectForMessage();
        copy.data = data == null ? null : new ArrayList<>(data);
        return copy;
    }
}
