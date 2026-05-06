package ru.otus.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.otus.annotation.Auditable;
import ru.otus.kafka.AuditEvent;
import ru.otus.kafka.KafkaAuditEventProducer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

   private final KafkaAuditEventProducer producer;

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void audit(JoinPoint joinPoint,
                      Auditable auditable,
                      Object result) {
        try {
            AuditEvent event = AuditEvent.builder()
                    .action(auditable.action().name())
                    .username(getCurrentUserName())
                    .entityName(auditable.entity().name())
                    .method(joinPoint.getSignature().getName())
                    .args(getArgs(joinPoint))
                    .result(result)
                    .resultClass(result!= null ? result.getClass(): null)
                    .callStatus("OK")
                    .timestamp(LocalDateTime.now())
                    .build();
            if (auditable.idFieldName() != null) {
                event.setIdFieldValue(extractIdFromMethodArgs(joinPoint, auditable.idFieldName()));
            }
            if (result instanceof List<?> list && !list.isEmpty()) {
                Object first = list.get(0);
                event.setResultGenericTypeName(first.getClass().getName());
            }
            producer.sendEvent(event);
        } catch (Exception e) {
            log.error("Failed to send audit event", e);
        }
    }

    @AfterThrowing(pointcut = "@annotation(auditable)", throwing = "ex")
    public void auditError(JoinPoint joinPoint, Auditable auditable, Exception ex) {
        try {
            AuditEvent event = AuditEvent.builder()
                    .entityName(auditable.entity().name())
                    .action(auditable.action().name())
                    .username(getCurrentUserName())
                    .method(joinPoint.getSignature().getName())
                    .timestamp(LocalDateTime.now())
                    .callStatus("ERROR: " + ex.getMessage())
                    .build();
            if (auditable.idFieldName() != null) {
                event.setIdFieldValue(extractIdFromMethodArgs(joinPoint, auditable.idFieldName()));
            }

            producer.sendEvent(event);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentUserName () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails) authentication.getPrincipal();
        var result = userDetails.getUsername();
        return result;
    }

    private Long extractIdFromMethodArgs(JoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for (var i = 0; i < paramNames.length; i++){
            if (paramName.equals(paramNames[i])) {
                if (args[i] instanceof Long paramValue) {
                    return  paramValue;
                }
            }
        }
        return null;
    }

    private Map<String, Object> getArgs(JoinPoint joinPoint) {
        Map<String, Object> argsMap = new HashMap<>();

        Object[] args = joinPoint.getArgs();
        String[] names = ((org.aspectj.lang.reflect.CodeSignature) joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < args.length; i++) {
            argsMap.put(names[i], args[i]);
        }

        return argsMap;
    }
}