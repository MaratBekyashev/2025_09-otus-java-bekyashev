package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.dto.ClientDto;

public interface ClientService {

    ClientDto saveClient(ClientDto client);

    List<ClientDto> findAll();

    ClientDto getClient(long id);

    void deleteClient(Long clientId);
}
