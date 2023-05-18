package fr.guen.dev.sgm.rest;

import fr.guen.dev.sgm.common.mapper.UserMapper;
import fr.guen.dev.sgm.payload.common.UserInfoDTO;
import fr.guen.dev.sgm.payload.request.SignInRequest;
import fr.guen.dev.sgm.payload.request.SignUpRequest;
import fr.guen.dev.sgm.payload.response.JwtAuthenticationResponse;
import fr.guen.dev.sgm.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sgm/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(userService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(userService.signIn(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        try {
            return ResponseEntity.ok(
                    UserMapper.mepUserListToUserInfoDtoList(userService.getAllUsers())
            );
        } catch(Exception exception) {
            log.error("Error : {}", exception.getMessage());
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
