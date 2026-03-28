package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Project;
import cat.udl.eps.softarch.demo.repository.ProjectRepository;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class ProjectEventHandler {
    final ProjectRepository projectRepository;

    public ProjectEventHandler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @HandleBeforeCreate
    public void handleProjectPreCreate(Project project) {
        ZonedDateTime timeStamp = ZonedDateTime.now();
        project.setCreated(timeStamp);
        project.setModified(timeStamp);
    }

    @HandleBeforeSave
    public void handleProjectPreSave(Project project) {
        ZonedDateTime timeStamp = ZonedDateTime.now();
        project.setModified(timeStamp);
    }
}