package nl.itvitae.rooster.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsernameIgnoreCase(username).orElseThrow(
        () -> new UsernameNotFoundException(
            String.format("User with username %s does not exist", username)));
  }
}
