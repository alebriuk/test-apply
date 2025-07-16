package com.challenge.salasia.web.dto;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        List<FieldErrorResponse> errors
) {}
