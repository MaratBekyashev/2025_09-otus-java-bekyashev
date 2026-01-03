alter table phone add CONSTRAINT phone_client_fk FOREIGN KEY (client_id) REFERENCES client (id);

alter table client add CONSTRAINT client_address_fk FOREIGN KEY (address_id) REFERENCES address (id);

