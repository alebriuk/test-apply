package com.challenge.salasia.web.dto;

import com.challenge.salasia.validators.ValidMonth;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ArticleSearchRequest(
        String author,
        String title,
        String tag,
        @ValidMonth String month,
        @Min(0) Integer page,
        @Max(5) Integer size
) {}

