package vn.edu.usth.doconcall.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.usth.doconcall.Models.UserDto;

public interface UserAPI {

    @GET("backend/user/get/{id}")
    Call<UserDto> getUserById(@Header("Authorization") String authorizationHeader, @Path("id") int user_id);

    @GET("backend/user/get-all-user")
    Call<List<UserDto>> getAllUsers(@Header("Authorization") String authorizationHeader, @Query("name") String user_name);

    @GET("backend/user/check/{phoneNumber}")
    Call<Boolean> checkPhoneNumber(@Path("phoneNumber") String phoneNumber);

    @PUT("backend/user/update/{id}")
    Call<UserDto> updateUser(@Header("Authorization") String authorizationHeader, @Path("id") int user_id, @Body UserDto userDto);
}
