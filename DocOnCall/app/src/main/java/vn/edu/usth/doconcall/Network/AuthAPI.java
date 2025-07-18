package vn.edu.usth.doconcall.Network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import vn.edu.usth.doconcall.Models.AuthResponse;
import vn.edu.usth.doconcall.Models.AuthenticationRequest;
import vn.edu.usth.doconcall.Models.RegisterRequest;

public interface AuthAPI {

    @POST("backend/authenticate/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("backend/authenticate/auth")
    Call<AuthResponse> login(@Body AuthenticationRequest request);

    @POST("backend/authenticate/logout")
    Call<Void> logout(@Header("Authorization") String authorizationHeader);
}
