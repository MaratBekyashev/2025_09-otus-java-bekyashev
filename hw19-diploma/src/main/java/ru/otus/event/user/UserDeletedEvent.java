package ru.otus.event.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDeletedEvent implements UserEvent{

    private Long userId;

    private LocalDateTime eventDate;

}