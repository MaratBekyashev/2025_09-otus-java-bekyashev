package ru.otus.hw.dao;

import java.util.Set;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CLIENTS")
@ToString
public class Client {
    @Id
    @Column("CLIENT_ID")
    private Long clientId;

    @Column("NAME")
    private String name;

    @MappedCollection(idColumn = "CLIENT_ID")
    Set<Phone> phones;
}
