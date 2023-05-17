package fr.guen.dev.sgm.security.jwt.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUserName(String token);
    String generateToken(UserDetails userDetails);
    boolean istokenValid(String token, UserDetails userDetails);

}
