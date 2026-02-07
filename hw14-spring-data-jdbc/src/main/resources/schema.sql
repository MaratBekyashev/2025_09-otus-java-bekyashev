create table CLIENTS (
    client_id bigserial,
    name      varchar(255),
    primary key (client_id)
);

create table PHONES
(
    phone_id   bigserial not null primary key,
    msisdn     varchar(50),
    client_id  bigint
);