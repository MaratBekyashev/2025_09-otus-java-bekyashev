package ru.otus.kafka;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.otus.model.IdentifableEntity;
import ru.otus.service.AuditService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventConsumer {

    private final ObjectMapper objectMapper;
    private final AuditService auditService;

    @KafkaListener(topics = "audit-topic", groupId = "audit-service")
    public void consume(AuditEvent event) {
        Object result = event.getResult();
        Object callResult = null;

        if (result != null &&
            "OK".equals(event.getCallStatus())) {
            if (result instanceof List<?>) {
                try {
                    JavaType type = objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, Class.forName(event.getResultGenericTypeName()));
                    callResult = objectMapper.convertValue(event.getResult(), type);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (event.getResultClass() != null) {
                callResult = objectMapper.convertValue(event.getResult(), event.getResultClass());
            }
        }
        List<Long> entityIdList = Optional
                .ofNullable(extractIdentifiableEntityId(callResult))
                .orElse(Collections.singletonList(event.getIdFieldValue()));

        for (Long entityId : entityIdList) {
            String res = String.valueOf(result);
            auditService.log(event.getEntityName(), entityId, event.getAction(), event.getUsername(), res,
                    event.getCallStatus());
        }
    }

    private List<Long> extractIdentifiableEntityId(Object result) {
        if (result == null) {
            return null;
        }
        if (result instanceof IdentifableEntity enity) {
            Long id = enity.getId();
            return Collections.singletonList(id);
        }

        if (result instanceof List<?> list &&
                list.size() > 0 &&
                list.get(0) instanceof IdentifableEntity ) {
            List<Long> resultList = list.stream()
                    .map(e -> ((IdentifableEntity)e).getId())
                    .toList();
            return resultList;
        }

        return null;
    }

}