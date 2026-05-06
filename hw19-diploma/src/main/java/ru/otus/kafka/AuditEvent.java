package ru.otus.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEvent {
    private String action;

    private String username;

    private String method;

    private String entityName;

    private Map<String, Object> args;

    private Object result;

    private String callStatus;

    private Class<?> resultClass;

    private String resultGenericTypeName;

    private LocalDateTime timestamp;

    private Long idFieldValue;
}