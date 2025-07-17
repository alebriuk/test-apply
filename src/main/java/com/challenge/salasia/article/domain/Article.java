package com.challenge.salasia.article.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(
    name = "articles",
    indexes = {
      @Index(name = "idx_article_author", columnList = "author"),
      @Index(name = "idx_article_created", columnList = "createdAt"),
      @Index(name = "idx_article_story_id", columnList = "storyId")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "tags")
@EqualsAndHashCode(of = "objectId")
public class Article {

  @Id
  @Column(nullable = false, updatable = false, length = 50)
  private String objectId;

  @Column(nullable = false, length = 100)
  private String author;

  @Lob
  @Column(columnDefinition = "TEXT")
  private String commentText;

  private Instant createdAt;
  private Instant updatedAt;

  private Long parentId;
  private Long storyId;

  @Column(nullable = false, length = 500)
  private String storyTitle;

  private String storyUrl;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "article_tags",
      joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "objectId"),
      indexes = @Index(name = "idx_article_tag", columnList = "tag"))
  @Column(name = "tag", length = 50)
  @Builder.Default
  private List<String> tags = new ArrayList<>();

  @Column(nullable = false)
  @Builder.Default
  private boolean deleted = false;
}
