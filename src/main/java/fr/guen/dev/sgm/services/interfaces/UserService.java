package fr.guen.dev.sgm.services.interfaces;

import fr.guen.dev.sgm.models.User;
import fr.guen.dev.sgm.payload.common.UserInfoDTO;
import fr.guen.dev.sgm.payload.request.SignInRequest;
import fr.guen.dev.sgm.payload.request.SignUpRequest;
import fr.guen.dev.sgm.payload.response.JwtAuthenticationResponse;

import java.util.List;

public interface UserService {

    JwtAuthenticationResponse signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
    List<User> getAllUsers();
    User update(Integer id, UserInfoDTO userInfoDTO);

}
