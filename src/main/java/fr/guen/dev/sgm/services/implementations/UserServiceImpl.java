package fr.guen.dev.sgm.services.implementations;

import fr.guen.dev.sgm.common.enums.Role;
import fr.guen.dev.sgm.common.enums.Status;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            if(validateSignUpRequest(signUpRequest)) {
                //Check if user exist in database
                final var userExist = userRepository.findByEmail(signUpRequest.getEmail());

                if (userExist.isEmpty()) { //Email does not exist yet
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
            } else {
                return DefaultResponse.builder()
                        .jwtAuthenticationResponse(new JwtAuthenticationResponse(null))
                        .message("Invalid data.")
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
            if(user.getStatus().equals(Status.FALSE)){ //check if user status is not activated
                return DefaultResponse.builder()
                        .jwtAuthenticationResponse(new JwtAuthenticationResponse(null))
                        .message("Wait for admin approval.")
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .build();
            }

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

    private boolean validateSignUpRequest(SignUpRequest signUpRequest) {
        return !StringUtils.isBlank(signUpRequest.getFirstName()) && !StringUtils.isBlank(signUpRequest.getLastName())
                && !StringUtils.isBlank(signUpRequest.getEmail()) && !StringUtils.isBlank(signUpRequest.getPassword())
                && !StringUtils.isBlank(signUpRequest.getContactNumber());
    }

    private User getUserFromSignUpRequest(SignUpRequest signUpRequest) {
        return User.builder()
                .firsName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .contactNumber(signUpRequest.getContactNumber())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .status(Status.FALSE)
                .build();
    }
}













