package ru.otus.event.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectCreatedEvent implements ProjectEvent{

    private String title;

    private Long projectId;
}