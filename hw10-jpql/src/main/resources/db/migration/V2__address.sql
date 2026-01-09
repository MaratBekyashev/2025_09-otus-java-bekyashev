-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence address_SEQ start with 1 increment by 1;

create table address
(
    id     bigint not null primary key,
    street varchar(50)
);
