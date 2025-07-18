package vn.edu.usth.doconcall.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.usth.doconcall.Models.DoctorDto;

public interface DoctorAPI {

    @GET("backend/doctor/get/{id}")
    Call<DoctorDto> getDoctorById(@Header("Authorization") String authorizationHeader, @Path("id") int id);

    @GET("backend/doctor/get-all-doctor")
    Call<List<DoctorDto>> getAllDoctors(@Header("Authorization") String authorizationHeader, @Query("name") String name);

    @PUT("backend/doctor/update/{id}")
    Call<DoctorDto> updateDoctor(@Header("Authorization") String authorizationHeader, @Path("id") int id, @Body DoctorDto doctorDto);

}
