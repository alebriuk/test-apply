package com.challenge.salasia.article.domain;

import com.challenge.salasia.article.api.model.AlgoliaHit;
import java.util.List;

public class ArticleMapper {

  public static Article toEntity(AlgoliaHit hit) {
    return Article.builder()
        .objectId(hit.objectId())
        .author(hit.author())
        .commentText(hit.commentText())
        .createdAt(hit.createdAt())
        .updatedAt(hit.updatedAt())
        .parentId(hit.parentId())
        .storyId(hit.storyId())
        .storyTitle(hit.storyTitle())
        .storyUrl(hit.storyUrl())
        .tags(hit.tags() != null ? hit.tags() : List.of())
        .deleted(false)
        .build();
  }
}
