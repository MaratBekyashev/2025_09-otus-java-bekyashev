package ru.otus.event.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.model.task.TaskStatusEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserCreatedEvent implements UserEvent{

    private Long userId;

    private String login;

    private String userFullName;

    private String email;

    private LocalDateTime eventDate;

}