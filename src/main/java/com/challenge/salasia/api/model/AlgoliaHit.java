package com.challenge.salasia.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;

public record AlgoliaHit(
        @JsonProperty("objectID") String objectId,
        String author,
        @JsonProperty("comment_text") String commentText,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("parent_id") Long parentId,
        @JsonProperty("story_id") Long storyId,
        @JsonProperty("story_title") String storyTitle,
        @JsonProperty("story_url") String storyUrl,
        @JsonProperty("_tags") List<String> tags
) {}
