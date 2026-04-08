package ru.otus.event.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.otus.model.ProjectRoleEnum;

@Getter
@Setter
@NoArgsConstructor
public class ProjectMemberAddedEvent implements ProjectEvent{

    private Long projectMemberId;

    private Long projectId;

    ProjectRoleEnum memberRole;
}