package ru.otus.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "address")
@NoArgsConstructor
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq", allocationSize = 1)
    private Long id;

    @Column(name = "street")
    private String street;

    public Address(Long id, String street) {
        this(street);
        this.id = id;
    }

    public Address(String street) {
        this.street = street;
    }
}
