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
import vn.edu.usth.doconcall.Models.ScheduleEventDto;

public interface ScheduleEventAPI {

    @POST("backend/schedule-event")
    Call<ScheduleEventDto> createScheduleEvent(@Header("Authorization") String authorizationHeader, @Body ScheduleEventDto dto);

    @GET("backend/schedule-event/patient/{id}")
    Call<List<ScheduleEventDto>> getByPatientId(@Header("Authorization") String authorizationHeader, @Path("id") int patientId);

    @GET("backend/schedule-event/doctor/{id}")
    Call<List<ScheduleEventDto>> getByDoctorId(@Header("Authorization") String authorizationHeader, @Path("id") int doctorId);

    @GET("backend/schedule-event/get-all")
    Call<List<ScheduleEventDto>> getAllScheduleEvents(@Header("Authorization") String authorizationHeader);

    @PUT("backend/schedule-event/update/{id}")
    Call<ScheduleEventDto> updateScheduleEvent(@Header("Authorization") String authorizationHeader, @Path("id") int eventId, @Body ScheduleEventDto dto);

    @DELETE("backend/schedule-event/delete/{id}")
    Call<ResponseBody> deleteScheduleEvent(@Header("Authorization") String authorizationHeader, @Path("id") int eventId);
}
