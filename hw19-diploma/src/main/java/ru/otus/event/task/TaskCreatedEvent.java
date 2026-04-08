package ru.otus.event.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskCreatedEvent implements TaskEvent {

    private Long taskId;

    private String title;

    private Long projectId;
}