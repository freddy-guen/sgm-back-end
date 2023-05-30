package fr.guen.dev.sgm.rest;

import fr.guen.dev.sgm.common.exceptions.ApiError;
import fr.guen.dev.sgm.common.mapper.UserMapper;
import fr.guen.dev.sgm.models.User;
import fr.guen.dev.sgm.payload.common.UserInfoDTO;
import fr.guen.dev.sgm.payload.request.SignInRequest;
import fr.guen.dev.sgm.payload.request.SignUpRequest;
import fr.guen.dev.sgm.payload.response.JwtAuthenticationResponse;
import fr.guen.dev.sgm.security.jwt.services.interfaces.JwtService;
import fr.guen.dev.sgm.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/sgm/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(userService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(userService.signIn(request));
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() throws ApiError {
        if(jwtService.isAdmin()){
            return new ResponseEntity<>(
                    UserMapper.mepUserListToUserInfoDtoList(userService.getAllUsers()),
                    HttpStatus.OK);
        }else {
            //return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            throw new ApiError(HttpStatus.UNAUTHORIZED, "L'utilisateur n'est pas autorisé");
        }
    }


    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() throws ApiError {

        Map < String, Object > jsonResponseMap = new LinkedHashMap<> ();
        if(jwtService.isAdmin()){
            List<UserInfoDTO> listDTOs = UserMapper.mepUserListToUserInfoDtoList(userService.getAllUsers());
            jsonResponseMap.put("status", 1);
            jsonResponseMap.put("data", listDTOs);

            return new ResponseEntity < > (jsonResponseMap, HttpStatus.OK);
        }else {
            //return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            jsonResponseMap.put("status", HttpStatus.UNAUTHORIZED.value());
            jsonResponseMap.put("data", "L'utilisateur n'est pas autorisé");

            return new ResponseEntity < > (jsonResponseMap, HttpStatus.UNAUTHORIZED);
        }
    }



    @PutMapping("{id}/update")
    public ResponseEntity<UserInfoDTO> update(@PathVariable Integer id, @RequestBody UserInfoDTO request){

        if(jwtService.isAdmin()){
            return new ResponseEntity<>(
                    UserMapper.mapUserToUserInfoDTO(userService.update(id, request)),
                    HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new UserInfoDTO(), HttpStatus.UNAUTHORIZED);

        }
    }
}
