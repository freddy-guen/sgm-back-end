package fr.guen.dev.sgm.security.jwt.services.implementations;

import fr.guen.dev.sgm.repository.UserRepository;
import fr.guen.dev.sgm.security.jwt.services.interfaces.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUserDetailsServiceImpl implements CustomerUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService() {
        log.info("Inside userDetailsService.");
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
            }
        };
    }
}
