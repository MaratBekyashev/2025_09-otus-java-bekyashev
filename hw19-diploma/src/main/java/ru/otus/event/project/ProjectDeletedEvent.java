package ru.otus.event.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDeletedEvent implements ProjectEvent {

    private Long projectId;

    private String title;

}