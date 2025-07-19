package com.challenge.salasia.common.dto;

import java.util.List;

public record ValidationErrorResponse(int status, List<FieldErrorResponse> errors) {}
