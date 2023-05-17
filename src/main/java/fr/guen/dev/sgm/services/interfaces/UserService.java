package fr.guen.dev.sgm.services.interfaces;

import fr.guen.dev.sgm.payload.request.SignInRequest;
import fr.guen.dev.sgm.payload.request.SignUpRequest;
import fr.guen.dev.sgm.payload.response.DefaultResponse;

public interface UserService {

    DefaultResponse signUp(SignUpRequest signUpRequest);
    DefaultResponse signIn(SignInRequest signInRequest);


}
