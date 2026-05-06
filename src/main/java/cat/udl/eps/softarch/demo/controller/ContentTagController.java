package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.Content;
import cat.udl.eps.softarch.demo.domain.Tag;
import cat.udl.eps.softarch.demo.repository.ContentRepository;
import cat.udl.eps.softarch.demo.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ContentTagController {

    private final ContentRepository contentRepository;
    private final TagRepository tagRepository;

    public ContentTagController(ContentRepository contentRepository, TagRepository tagRepository) {
        this.contentRepository = contentRepository;
        this.tagRepository = tagRepository;
    }

    @PutMapping(value = "/contents/{contentId}/tags", consumes = "text/uri-list")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<Void> assignTags(@PathVariable Long contentId, @RequestBody String uriList) {
        List<Long> tagIds = parseTagIds(uriList);

        Content content = contentRepository.findByContentIdWithTagsForUpdate(contentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (content.getTags() == null) {
            content.setTags(new ArrayList<>());
        }

        for (Long tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            if (!content.getTags().contains(tag)) {
                content.getTags().add(tag);
            }
        }

        contentRepository.save(content);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/contents/{contentId}/tags/{tagId}")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<Void> removeTag(@PathVariable Long contentId, @PathVariable Long tagId) {
        Content content = contentRepository.findByContentIdWithTagsForUpdate(contentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        tagRepository.findById(tagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (content.getTags() != null) {
            content.getTags().removeIf(tag -> tagId.equals(tag.getId()));
            contentRepository.save(content);
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tags/{tagId}/delete")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<Content> taggedContents = contentRepository.findByTags_Id(tagId);
        for (Content content : taggedContents) {
            if (content.getTags() != null) {
                content.getTags().removeIf(existingTag -> tagId.equals(existingTag.getId()));
            }
        }
        contentRepository.saveAll(taggedContents);
        tagRepository.delete(tag);

        return ResponseEntity.noContent().build();
    }

    @GetMapping({"/tags/{tagId}/available-contents", "/tags/{tagId}/avaiable-contents"})
    public ResponseEntity<List<Content>> getAvailableContents(@PathVariable Long tagId) {
        tagRepository.findById(tagId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(contentRepository.findAvailableForTagId(tagId));
    }

    private List<Long> parseTagIds(String uriList) {
        if (uriList == null || uriList.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Long> tagIds = Arrays.stream(uriList.split("\\R"))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .filter(line -> !line.startsWith("#"))
                .map(this::parseTagId)
                .toList();

        if (tagIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return tagIds;
    }

    private Long parseTagId(String tagUri) {
        try {
            URI uri = URI.create(tagUri);
            String path = uri.getPath();
            if (path == null || path.isBlank()) {
                throw new IllegalArgumentException("Tag URI has no path");
            }

            String[] segments = Arrays.stream(path.split("/"))
                    .filter(segment -> !segment.isBlank())
                    .toArray(String[]::new);
            if (segments.length < 2 || !"tags".equals(segments[segments.length - 2])) {
                throw new IllegalArgumentException("Tag URI must point to /tags/{id}");
            }

            String id = segments[segments.length - 1];
            return Long.valueOf(id);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed tag URI", ex);
        }
    }
}
