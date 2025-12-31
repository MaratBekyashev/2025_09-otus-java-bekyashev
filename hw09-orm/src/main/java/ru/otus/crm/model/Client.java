package ru.otus.crm.model;

import lombok.*;
import ru.otus.jdbc.annotations.MyId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {

    @MyId
    private Long id;

    private String name;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }
}
