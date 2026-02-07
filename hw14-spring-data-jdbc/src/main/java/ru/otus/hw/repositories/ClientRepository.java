package ru.otus.hw.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.otus.hw.dao.Client;
import ru.otus.hw.dto.ClientWithPhonesRow;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Query(
            """
        SELECT c.client_id, c.name AS client_name, p.msisdn AS phone
        FROM CLIENTS c
        LEFT JOIN PHONES p ON p.client_id = c.client_id
    """)
    List<ClientWithPhonesRow> customFindAll();

    Optional<Client> findById(long id);
}
