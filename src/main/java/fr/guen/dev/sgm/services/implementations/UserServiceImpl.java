package fr.guen.dev.sgm.services.implementations;

import fr.guen.dev.sgm.common.enums.Role;
import fr.guen.dev.sgm.common.enums.Status;
import fr.guen.dev.sgm.common.mapper.UserMapper;
import fr.guen.dev.sgm.models.User;
import fr.guen.dev.sgm.payload.common.UserInfoDTO;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest signUpRequest) {
        log.info("Inside signUp : {}", signUpRequest);
        try {
            if(validateSignUpRequest(signUpRequest)) {
                //Check if user exist in database
                final var userExist = userRepository.findByEmail(signUpRequest.getEmail());

                if (userExist.isEmpty()) { //Email does not exist yet
                    final var user = userRepository.save(getUserFromSignUpRequest(signUpRequest));
                    var jwt = jwtService.generateToken(user);

                    return JwtAuthenticationResponse.builder()
                            .token(jwt)
                            .responseInfo(
                                    new DefaultResponse("User registered successfully.", HttpStatus.OK)
                            ).build();
                } else { //Email already exist
                    return JwtAuthenticationResponse.builder()
                            .token(null)
                            .responseInfo(
                                    new DefaultResponse("Email already exist.", HttpStatus.BAD_REQUEST)
                            ).build();
                }
            } else {
                return JwtAuthenticationResponse.builder()
                        .token(null)
                        .responseInfo(
                                new DefaultResponse("Invalid data.", HttpStatus.BAD_REQUEST)
                        ).build();
            }
        } catch (Exception exception) {
            log.error("Error : {}", exception.getMessage());
        }

        return JwtAuthenticationResponse.builder()
                .token(null)
                .responseInfo(
                        new DefaultResponse("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR)
                ).build();
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        log.info("Inside SignIn : {}", signInRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword())
            );
            var user = userRepository.findByEmail(signInRequest.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
            if(user.getStatus().equals(Status.FALSE)){ //check if user status is not activated
                return JwtAuthenticationResponse.builder()
                        .token(null)
                        .responseInfo(
                                new DefaultResponse("Wait for admin approval.", HttpStatus.NOT_FOUND)
                        ).build();
            }

            var jwt = jwtService.generateToken(user);

            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .responseInfo(
                            new DefaultResponse("User login with success.", HttpStatus.OK)
                    ).build();
        } catch (Exception exception) {
            log.error("Error : {}", exception.getMessage());
        }

        return JwtAuthenticationResponse.builder()
                .token(null)
                .responseInfo(
                        new DefaultResponse("Invalid email or password.", HttpStatus.NOT_FOUND)
                ).build();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User update(Integer id, UserInfoDTO userInfoDTO) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            if(validateUpdateUserRequest(userInfoDTO)) {
                user.get().setFirsName(userInfoDTO.getFirstName());
                user.get().setLastName(userInfoDTO.getLastName());
                user.get().setEmail(userInfoDTO.getEmail());
                if (!StringUtils.isBlank(userInfoDTO.getContactNumber())) {
                    user.get().setContactNumber(userInfoDTO.getContactNumber());
                }
                if (userInfoDTO.getRole() != null) {
                    user.get().setRole(userInfoDTO.getRole());
                }
                if (userInfoDTO.getStatus() != null) {
                    user.get().setStatus(userInfoDTO.getStatus());
                }

                return userRepository.save(user.get());
            } else {
                //TODO à implémenter
            }
        }else {
            //TODO à implémenter
        }
        return null;
    }

    private boolean validateSignUpRequest(SignUpRequest signUpRequest) {
        return !StringUtils.isBlank(signUpRequest.getFirstName()) && !StringUtils.isBlank(signUpRequest.getLastName())
                && !StringUtils.isBlank(signUpRequest.getEmail()) && !StringUtils.isBlank(signUpRequest.getPassword())
                && !StringUtils.isBlank(signUpRequest.getContactNumber());
    }

    private boolean validateUpdateUserRequest(UserInfoDTO userInfoDTO){
        return !StringUtils.isBlank(userInfoDTO.getFirstName()) && !StringUtils.isBlank(userInfoDTO.getLastName())
                && !StringUtils.isBlank(userInfoDTO.getEmail());
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













