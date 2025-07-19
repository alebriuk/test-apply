package com.challenge.salasia.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login credentials")
public record LogInRequest(
    @Schema(description = "Username", example = "admin") String username,
    @Schema(description = "Password", example = "pass") String password) {}
