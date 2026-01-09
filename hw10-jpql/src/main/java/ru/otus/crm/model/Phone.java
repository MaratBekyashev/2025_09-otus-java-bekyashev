package ru.otus.crm.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "phone")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "client")
public class Phone {

    @Id
    @SequenceGenerator(name = "phone_gen", sequenceName = "phone_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
    private Long id;

    @Column(name = "number")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Phone(Long id, String phoneNumber) {
        this(phoneNumber);
        this.id = id;
    }
}
