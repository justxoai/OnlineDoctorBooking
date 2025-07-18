package vn.edu.usth.doconcall.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import vn.edu.usth.doconcall.R;

public class ChangePassword_Activity extends AppCompatActivity {

    EditText phone_num, new_pass, confirm_pass;
    Button confirm_button;

    TextView no_phone, no_new_password, no_confirm_password, match_password;

    boolean valid = true;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Layout
        setContentView(R.layout.activity_change_password);

        // Setup Firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Setup ID
        phone_num = findViewById(R.id.input_phone_change_pass);
        new_pass = findViewById(R.id.input_new_pass_change_pass);
        confirm_pass = findViewById(R.id.input_confirm_new_pass_change_pass);

        confirm_button = findViewById(R.id.confirm_button);

        // Setup UI for Error
        no_phone = findViewById(R.id.error_no_phone_num);
        no_new_password = findViewById(R.id.error_no_password);
        no_confirm_password = findViewById(R.id.error_no_confirm_password);
        match_password = findViewById(R.id.error_match_password);

        no_phone.setVisibility(View.GONE);
        no_new_password.setVisibility(View.GONE);
        no_confirm_password.setVisibility(View.GONE);
        match_password.setVisibility(View.GONE);

        // Change Password function
        change_password_function();
    }

    private void change_password_function(){
        // Backward button
        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(view -> {
            Intent i = new Intent(ChangePassword_Activity.this, Login_Activity.class);
            startActivity(i);
            finish();
        });

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_phone();
                check_password();

                if(valid){
                    fStore.collection("Patient").whereEqualTo("PhoneNumber", phone_num.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            FirebaseUser fUser = fAuth.getCurrentUser();

                            // Chưa update đc password
                            fUser.updatePassword(confirm_pass.getText().toString());

                            Intent i = new Intent(ChangePassword_Activity.this, Login_Activity.class);
                            startActivity(i);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePassword_Activity.this, "Incorrect Phone Number", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public boolean check_phone(){
        if (phone_num.getText().toString().trim().isEmpty()) {
            no_phone.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            no_phone.setVisibility(View.GONE);
            valid = true;
        }

        return valid;
    }

    public boolean check_password(){
        if (new_pass.getText().toString().isEmpty()) {
            no_new_password.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            no_new_password.setVisibility(View.GONE);
            valid = true;
        }

        if (confirm_pass.getText().toString().isEmpty()) {
            no_confirm_password.setVisibility(View.VISIBLE);

            valid = false;
        } else if (!confirm_pass.equals(new_pass)) {
            no_confirm_password.setVisibility(View.GONE);
            match_password.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            no_confirm_password.setVisibility(View.GONE);
            match_password.setVisibility(View.GONE);
            valid = true;
        }

        return valid;
    }



}