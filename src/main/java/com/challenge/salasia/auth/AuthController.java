package com.challenge.salasia.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for login and token generation")
public class AuthController {

  private final JwtUtil jwtUtil;

  @Operation(
      summary = "Login with username and password",
      description = "Returns a JWT token if credentials are valid")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful login",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(example = "{\"token\": \"<JWT_TOKEN>\"}"))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
      })
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    String username = request.username();
    String password = request.password();

    if ("admin".equals(username) && "pass".equals(password)) {
      String token = jwtUtil.generateToken(username);
      return ResponseEntity.ok(Map.of("token", token));
    }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
  }
}
