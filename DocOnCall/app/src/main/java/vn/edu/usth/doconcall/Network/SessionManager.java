package vn.edu.usth.doconcall.Network;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static SessionManager instance;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private String token;
    private String phoneNumber;
    private int userId;
    private String userName;
    private String role;

    // Private constructor to prevent direct instantiation
    private SessionManager(Context context) {
        prefs = context.getSharedPreferences("SessionData", Context.MODE_PRIVATE);
        editor = prefs.edit();
        loadSession();  // Load existing session if available
    }

    // First-time initialization with context
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    // Safe access after initialization
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SessionManager not initialized. Call getInstance(Context) first.");
        }
        return instance;
    }

    // Save session data
    public void saveSession(String token, int userId, String userName, String role) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.role = role;

        editor.putString("token", token);
        editor.putInt("userId", userId);
        editor.putString("userName", userName);
        editor.putString("role", role);
        editor.apply();
    }

    // Load session data into memory
    private void loadSession() {
        token = prefs.getString("token", null);
        phoneNumber = prefs.getString("phoneNumber", null);
        role = prefs.getString("role", null);
    }

    // Clear all session data
    public void clearSession() {
        editor.clear();
        editor.apply();

        token = null;
        phoneNumber = null;
        role = null;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public boolean isLoggedIn() {
        return token != null && !token.isEmpty();
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
