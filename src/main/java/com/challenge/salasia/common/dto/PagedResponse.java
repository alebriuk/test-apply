package com.challenge.salasia.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Paginated response wrapper")
public record PagedResponse<T>(
    @Schema(description = "List of items in the current page") List<T> content,
    @Schema(description = "Current page number (0-based)", example = "0") int page,
    @Schema(description = "Size of the page", example = "5") int size,
    @Schema(description = "Total number of elements", example = "42") long totalElements,
    @Schema(description = "Total number of pages", example = "9") int totalPages,
    @Schema(description = "Whether this is the last page", example = "false") boolean last) {}
