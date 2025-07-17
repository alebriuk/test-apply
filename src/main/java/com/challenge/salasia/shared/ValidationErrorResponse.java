package com.challenge.salasia.shared;

import java.util.List;

public record ValidationErrorResponse(int status, List<FieldErrorResponse> errors) {}
