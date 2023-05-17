package fr.guen.dev.sgm.rest;

import fr.guen.dev.sgm.payload.request.SignInRequest;
import fr.guen.dev.sgm.payload.request.SignUpRequest;
import fr.guen.dev.sgm.payload.response.DefaultResponse;
import fr.guen.dev.sgm.payload.response.JwtAuthenticationResponse;
import fr.guen.dev.sgm.services.interfaces.UserService;
import fr.guen.dev.sgm.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sgm/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<DefaultResponse> signUp(@RequestBody SignUpRequest request) {
        try {
            return ResponseEntity.ok(userService.signUp(request));
        } catch (Exception exception) {
            log.error("Error : {}", exception.getMessage());
        }

        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(userService.signIn(request));
    }

}
