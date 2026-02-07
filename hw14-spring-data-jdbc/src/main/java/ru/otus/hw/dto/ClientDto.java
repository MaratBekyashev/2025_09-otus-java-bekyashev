package ru.otus.hw.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.hw.dao.Client;
import ru.otus.hw.dao.Phone;

@Getter
@Setter
@NoArgsConstructor
public class ClientDto {

    private Long clientId;

    private String clientName;

    List<String> phones;

    private ClientDto(Client client) {
        this.clientId = client.getClientId();
        this.clientName = client.getName();
        this.phones = client.getPhones().stream().map(Phone::getPhoneNumber).collect(Collectors.toList());
    }

    public static ClientDto toDto(Client client) {
        return new ClientDto(client);
    }

    public static Client toDomain(ClientDto client) {
        Client result = new Client();
        result.setClientId(client.getClientId());
        result.setName(client.getClientName());
        if (client.getPhones() != null && client.getPhones().size() > 0) {
            var phones = client.getPhones().stream()
                    .filter(e -> !e.isBlank())
                    .map(Phone::new)
                    .collect(Collectors.toSet());
            result.setPhones(phones);
        }
        return result;
    }

    public static List<ClientDto> toDtoList(List<Client> clients) {
        return clients.stream().map(ClientDto::toDto).collect(Collectors.toList());
    }
}
