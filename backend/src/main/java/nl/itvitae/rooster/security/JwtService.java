package nl.itvitae.rooster.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import nl.itvitae.rooster.user.User;
import nl.itvitae.rooster.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  @Value("${app.jwt.secret}")
  private String JWT_SECRET;

  @Value("${app.jwt.expirationMs}")
  private int JWT_EXPIRATION_TIME;

  private final UserRepository userRepository;

  public String generateJwt(String username) {
    Optional<User> optUser = userRepository.findByUsernameIgnoreCase(username);
    if (optUser.isEmpty()) {
      return String.format("user with username %s does not exist", username);
    }
    Map<String, Object> claims = new HashMap<>();
    claims.put("roles", optUser.get().getAuthorities());
    return buildJwt(claims, username);
  }

  private String buildJwt(Map<String, Object> claims, String username) {
    Date currentDate = new Date();
    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(currentDate)
        .expiration(new Date(currentDate.getTime() + JWT_EXPIRATION_TIME))
        .signWith(getSignKey())
        .compact();
  }

  private SecretKey getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUsernameFromToken(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpirationDateFromToken(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    String username = extractUsernameFromToken(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpirationDateFromToken(token).before(new Date());
  }
}
