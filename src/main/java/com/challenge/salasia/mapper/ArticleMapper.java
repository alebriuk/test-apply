package com.challenge.salasia.mapper;

import com.challenge.salasia.api.model.AlgoliaHit;
import com.challenge.salasia.persistence.Article;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<Article> toEntityList(List<AlgoliaHit> hits) {
        return hits.stream()
                .map(ArticleMapper::toEntity)
                .collect(Collectors.toList());
    }
}
