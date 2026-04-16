package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@RepositoryEventHandler
public class TagEventHandler {

    final Logger logger = LoggerFactory.getLogger(TagEventHandler.class);

    final TagRepository tagRepository;

    public TagEventHandler(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @HandleBeforeCreate
    public void handleTagPreCreate(Tag tag) {
        logger.info("Before creating tag with name='{}'", tag.getName());

        ZonedDateTime timeStamp = ZonedDateTime.now();
        tag.setCreated(timeStamp);
        tag.setModified(timeStamp);

        logger.info("Tag ready to be created with name='{}'", tag.getName());
    }

    @HandleBeforeSave
    public void handleTagPreSave(Tag tag) {
        logger.info("Before saving tag id='{}', name='{}'", tag.getId(), tag.getName());

        tag.setModified(ZonedDateTime.now());

        logger.info("Tag ready to be saved id='{}', name='{}'", tag.getId(), tag.getName());
    }

    @HandleBeforeDelete
    public void handleTagPreDelete(Tag tag) {
        logger.info("Before deleting tag id='{}', name='{}'", tag.getId(), tag.getName());
    }

    @HandleAfterDelete
    public void handleTagPostDelete(Tag tag) {
        logger.info("Tag deleted id='{}', name='{}'", tag.getId(), tag.getName());
    }
}
