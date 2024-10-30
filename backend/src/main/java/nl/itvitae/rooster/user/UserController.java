package nl.itvitae.rooster.user;

import lombok.AllArgsConstructor;
import nl.itvitae.rooster.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  @PostMapping("login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.username(),
            loginRequest.password()));

    if (!authentication.isAuthenticated()) {
      return new ResponseEntity<>("invalid username/password", HttpStatus.UNAUTHORIZED);
    }
    return ResponseEntity.ok(jwtService.generateJwt(loginRequest.username()));

  }
}
