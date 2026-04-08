package ru.otus.event.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPasswordChangedEvent implements UserEvent{

    private Long userId;



}