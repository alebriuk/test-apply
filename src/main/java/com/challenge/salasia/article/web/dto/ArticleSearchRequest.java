package com.challenge.salasia.article.web.dto;

import com.challenge.salasia.article.web.validators.ValidMonth;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Schema(description = "Filters for searching articles")
public record ArticleSearchRequest(
    @Schema(description = "Author of the article", example = "John Doe") String author,
    @Schema(description = "Title of the article", example = "The Future of AI") String title,
    @Schema(description = "Tag for filtering articles", example = "technology") String tag,
    @Schema(description = "Month name", example = "september") @ValidMonth String month,
    @Schema(description = "Page number (0-based)", example = "0", defaultValue = "0") @Min(0)
        Integer page,
    @Schema(description = "Page size (max 5)", example = "5", defaultValue = "5") @Max(5)
        Integer size) {}
