package ru.otus.event.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserRolesChangedEvent implements UserEvent{

    private Long userId;

    private Set<String> newRoles;
}