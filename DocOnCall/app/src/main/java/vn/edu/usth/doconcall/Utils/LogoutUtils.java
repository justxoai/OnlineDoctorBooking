package vn.edu.usth.doconcall.Utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.usth.doconcall.Auth.Login_Activity;
import vn.edu.usth.doconcall.Network.AuthAPI;
import vn.edu.usth.doconcall.Network.RetrofitClient;
import vn.edu.usth.doconcall.Network.SessionManager;


public class LogoutUtils {
    private static LogoutUtils instance;

    private LogoutUtils() {
    }

    public static LogoutUtils getInstance() {
        if (instance == null) {
            instance = new LogoutUtils();
        }
        return instance;
    }

    public void logoutUser(Context context) {
        String token = SessionManager.getInstance().getToken();

        if (token != null && !token.isEmpty()) {
            AuthAPI authService = RetrofitClient.getInstance().create(AuthAPI.class);
            authService.logout("Bearer " + token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        SessionManager.getInstance().clearSession();

                        if (context instanceof FragmentActivity) {
                                Intent i = new Intent(context, Login_Activity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(i);
                        } else {
                            Toast.makeText(context, "Logged out but not an activity context", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(context, "Backend logout failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Network error during logout", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            SessionManager.getInstance().clearSession();
            if (context instanceof FragmentActivity) {
                Intent i = new Intent(context, Login_Activity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(i);
            } else {
                Toast.makeText(context, "No token, forced logout", Toast.LENGTH_SHORT).show();
            }
        }
    }
}