package ma.ensa.www.assistdoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class activity_sign_up extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText emailText, passwordText, nameText;
    private Button signUpBtn ;
    private TextView signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Firebase setup
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Users is already authenticated, so redirect to the MainActivityPatient (patient page)
            startActivity(new Intent(activity_sign_up.this, MainActivityPatient.class));
            finish();
        }

        // UI elements
        emailText = findViewById(R.id.email_reg);
        passwordText = findViewById(R.id.password_reg);
        signUpBtn = findViewById(R.id.reg_btn);

        signIn = findViewById(R.id.sign_up);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_sign_up.this, activity_sign_in.class));
            }
        });
        signUpBtn.setOnClickListener(v -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                signUpUser(email, password);
            } else {
                Toast.makeText(activity_sign_up.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("SignUp", "Users created: " + user.getUid());

                        // Add user to Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("email", email);
                        userData.put("type", "Patient"); // Default type

                        db.collection("Users").document(email).set(userData)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Users added to Firestore"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Failed to add user", e));

                        Toast.makeText(activity_sign_up.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity_sign_up.this, activity_sign_in.class));
                        finish();
                    } else {
                        Log.e("SignUp", "Error: ", task.getException());
                        Toast.makeText(activity_sign_up.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
