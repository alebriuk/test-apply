package com.challenge.salasia.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "articles")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article {

    @Id
    private String objectId;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String commentText;

    private Instant createdAt;
    private Instant updatedAt;

    private Long parentId;
    private Long storyId;

    private String storyTitle;
    private String storyUrl;

    @ElementCollection
    @CollectionTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_id",
            referencedColumnName = "objectId"))
    @Column(name = "tag")
    private List<String> tags;

    private boolean deleted;
}
