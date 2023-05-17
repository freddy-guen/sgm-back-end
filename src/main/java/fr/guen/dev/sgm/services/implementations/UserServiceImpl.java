package fr.guen.dev.sgm.services.implementations;

import fr.guen.dev.sgm.common.enums.Role;
import fr.guen.dev.sgm.models.User;
import fr.guen.dev.sgm.payload.request.SignInRequest;
import fr.guen.dev.sgm.payload.request.SignUpRequest;
import fr.guen.dev.sgm.payload.response.DefaultResponse;
import fr.guen.dev.sgm.payload.response.JwtAuthenticationResponse;
import fr.guen.dev.sgm.repository.UserRepository;
import fr.guen.dev.sgm.security.jwt.services.interfaces.JwtService;
import fr.guen.dev.sgm.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public DefaultResponse signUp(SignUpRequest signUpRequest) {
        log.info("Inside signUp : {}", signUpRequest);
        try {
            //Check if user exist in database
            final var userExist = userRepository.findByEmail(signUpRequest.getEmail());

            if(userExist.isEmpty()) { //Email does not exist yet
                final var user = userRepository.save(getUserFromSignUpRequest(signUpRequest));
                var jwt = jwtService.generateToken(user);

                return DefaultResponse.builder()
                        .jwtAuthenticationResponse(new JwtAuthenticationResponse(jwt))
                        .message("User registered successfully.")
                        .httpStatus(HttpStatus.OK)
                        .build();
            } else { //Email already exist
                return DefaultResponse.builder()
                        .jwtAuthenticationResponse(new JwtAuthenticationResponse(null))
                        .message("Email already exist.")
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .build();
            }
        } catch (Exception exception) {
            log.error("Error : {}", exception.getMessage());
        }

        return DefaultResponse.builder()
                .jwtAuthenticationResponse(new JwtAuthenticationResponse(null))
                .message("Something went wrong.")
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @Override
    public DefaultResponse signIn(SignInRequest signInRequest) {
        log.info("Inside SignIn : {}", signInRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
            );
            var user = userRepository.findByEmail(signInRequest.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            var jwt = jwtService.generateToken(user);

            return DefaultResponse.builder()
                    .jwtAuthenticationResponse(new JwtAuthenticationResponse(jwt))
                    .message("User login with success.")
                    .httpStatus(HttpStatus.OK)
                    .build();
        } catch (Exception exception) {
            log.error("Error : {}", exception.getMessage());
        }

        return DefaultResponse.builder()
                .jwtAuthenticationResponse(new JwtAuthenticationResponse(null))
                .message("Invalid email or password.")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
    }

    private User getUserFromSignUpRequest(SignUpRequest signUpRequest) {
        return User.builder()
                .firsName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .contactNumber(signUpRequest.getContactNumber())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();
    }
}













