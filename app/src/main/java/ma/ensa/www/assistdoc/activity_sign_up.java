package ma.ensa.www.assistdoc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private EditText usernameText, emailText, passwordText;
    private Button signUpBtn;
    private TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialisation de Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Redirection si l'utilisateur est déjà authentifié
            startActivity(new Intent(activity_sign_up.this, MainActivityPatient.class));
            finish();
        }

        // Initialisation des éléments UI
        usernameText = findViewById(R.id.username);
        emailText = findViewById(R.id.email_reg);
        passwordText = findViewById(R.id.password_reg);
        signUpBtn = findViewById(R.id.reg_btn);
        signIn = findViewById(R.id.sign_up);

        signIn.setOnClickListener(v -> startActivity(new Intent(activity_sign_up.this, activity_sign_in.class)));

        signUpBtn.setOnClickListener(v -> {
            String username = usernameText.getText().toString();
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            if (!email.isEmpty() && !password.isEmpty() && !username.isEmpty()) {
                signUpUser(email, password, username);
            } else {
                Toast.makeText(activity_sign_up.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signUpUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("SignUp", "User created: " + user.getUid());

                        // Ajouter l'utilisateur à Firestore sans stocker le mot de passe
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", username);
                        userData.put("email", email);
                        userData.put("type", "Patient"); // Exemple de type par défaut

                        // Utilisation de l'UID de Firebase pour le document
                        db.collection("Users").document(user.getUid())
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "User added to Firestore");
                                    Toast.makeText(activity_sign_up.this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(activity_sign_up.this, activity_sign_in.class));
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "Failed to add user", e);
                                    Toast.makeText(activity_sign_up.this, "Failed to add user to Firestore", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Log.e("SignUp", "Error: ", task.getException());
                        Toast.makeText(activity_sign_up.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
