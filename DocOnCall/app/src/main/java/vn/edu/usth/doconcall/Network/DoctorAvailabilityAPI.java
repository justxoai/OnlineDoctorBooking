package vn.edu.usth.doconcall.Network;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.edu.usth.doconcall.Models.Doctor_AvailabilityDto;

public interface DoctorAvailabilityAPI {

    @POST("backend/doctor-avail")
    Call<Doctor_AvailabilityDto> createDoctorAvailability(@Header("Authorization") String authorizationHeader, @Body Doctor_AvailabilityDto dto);

    @GET("backend/doctor-avail/get-id-date/{id}/{date}")
    Call<Doctor_AvailabilityDto> getAvailabilityByDoctorIdAndDate(@Header("Authorization") String authorizationHeader, @Path("id") int id, @Path("date") String date);

    @GET("backend/doctor-avail/get/{id}")
    Call<Doctor_AvailabilityDto> getAvailabilityById(@Header("Authorization") String authorizationHeader, @Path("id") int id);

    @GET("backend/doctor-avail/get-all-avail")
    Call<List<Doctor_AvailabilityDto>> getAllAvailabilities(@Header("Authorization") String authorizationHeader);

    @GET("backend/doctor-avail/get-all-avail/{id}")
    Call<List<Doctor_AvailabilityDto>> getAllByDoctorId(@Header("Authorization") String authorizationHeader, @Path("id") int doctorId);

    @PUT("backend/doctor-avail/update/{id}")
    Call<Doctor_AvailabilityDto> updateByDoctorId(@Header("Authorization") String authorizationHeader, @Path("id") int doctorId, @Body Doctor_AvailabilityDto dto);

    @PUT("backend/doctor-avail/update-id-date/{id}/{date}")
    Call<Doctor_AvailabilityDto> updateByIdAndDate(@Header("Authorization") String authorizationHeader, @Path("id") int doctorId, @Path("date") String date, @Body Doctor_AvailabilityDto dto);

    @DELETE("backend/doctor-avail/delete/{id}")
    Call<ResponseBody> deleteByDoctorId(@Header("Authorization") String authorizationHeader, @Path("id") int doctorId);

    @DELETE("backend/doctor-avail/delete-id-date/{id}/{date}")
    Call<ResponseBody> deleteByIdAndDate(@Header("Authorization") String authorizationHeader, @Path("id") int doctorId, @Path("date") String date);
}
