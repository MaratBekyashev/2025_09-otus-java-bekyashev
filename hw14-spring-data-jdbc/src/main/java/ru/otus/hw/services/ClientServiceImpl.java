package ru.otus.hw.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dao.Client;
import ru.otus.hw.dto.ClientDto;
import ru.otus.hw.dto.ClientWithPhonesRow;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.repositories.ClientRepository;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public ClientDto saveClient(ClientDto clientDto) {
        Client client = ClientDto.toDomain(clientDto);
        clientRepository.save(client);
        ClientDto result = ClientDto.toDto(client);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> findAll() {
        List<ClientWithPhonesRow> dataList = clientRepository.getAllClientsWithPhones();

        Map<Long, ClientDto> map = new LinkedHashMap<>();
        for (ClientWithPhonesRow row : dataList) {
            ClientDto dto = map.computeIfAbsent(row.getClientId(), id -> {
                ClientDto clnt = new ClientDto();
                clnt.setClientId(id);
                clnt.setClientName(row.getClientName());
                clnt.setPhones(new ArrayList<>());
                return clnt;
            });

            if (row.getPhone() != null) {
                dto.getPhones().add(row.getPhone());
            }
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public ClientDto getClient(long id) {
        var result = clientRepository
                .findById(id)
                .map(ClientDto::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
        return result;
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepository.deleteById(clientId);
    }
}
