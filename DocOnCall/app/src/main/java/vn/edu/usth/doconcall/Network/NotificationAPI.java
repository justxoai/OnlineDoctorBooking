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
import vn.edu.usth.doconcall.Models.NotificationDto;

public interface NotificationAPI {

    @POST("backend/notification")
    Call<NotificationDto> createNotification(@Header("Authorization") String authorizationHeader, @Body NotificationDto dto);

    @GET("backend/notification/get/{id}")
    Call<NotificationDto> getNotificationById(@Header("Authorization") String authorizationHeader, @Path("id") int id);

    @GET("backend/notification/get-all")
    Call<List<NotificationDto>> getAllNotifications(@Header("Authorization") String authorizationHeader);

    @PUT("backend/notification/update/{id}")
    Call<NotificationDto> updateNotification(@Header("Authorization") String authorizationHeader, @Path("id") int id, @Body NotificationDto dto);

    @DELETE("backend/notification/delete/{id}")
    Call<ResponseBody> deleteNotification(@Header("Authorization") String authorizationHeader, @Path("id") int id);
}
