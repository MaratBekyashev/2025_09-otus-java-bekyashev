package ru.otus.crm.model;

import lombok.*;
import ru.otus.jdbc.annotations.MyId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Manager {

    @MyId
    private Long no;

    private String label;

    private String param1;

    public Manager(String label) {
        this.label = label;
    }
}
