package vn.edu.usth.doconcall.Network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.edu.usth.doconcall.Models.PatientDto;

public interface PatientAPI {

    @GET("backend/patient/get/{id}")
    Call<PatientDto> getPatientById(@Header("Authorization") String authorizationHeader, @Path("id") int id);

    @GET("backend/patient/get-all-patient")
    Call<List<PatientDto>> getAllPatients(@Header("Authorization") String authorizationHeader, @Query("name") String name);

}
