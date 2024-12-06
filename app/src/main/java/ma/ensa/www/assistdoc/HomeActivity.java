package ma.ensa.www.assistdoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private static final String TAG = "MAIN_TAG";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog pd;
    LinearLayout L1, L2;
    Button bt1, bt2;
    TextView textview1, textview2;
    EditText editText1, editText2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        L1 = findViewById(R.id.L1);
        L2 = findViewById(R.id.L2);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        textview2 = findViewById(R.id.textview2);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        textview1 = findViewById(R.id.textview1);

        L1.setVisibility(View.VISIBLE);
        L2.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        pd.setTitle("Please wait...");
        pd.setCanceledOnTouchOutside(false);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                pd.dismiss();
                Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull @NotNull String verificationId, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG, "onCodeSent: " + verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                pd.dismiss();

                L1.setVisibility(View.GONE);
                L2.setVisibility(View.VISIBLE);

                Toast.makeText(HomeActivity.this, "Verification code sent...", Toast.LENGTH_SHORT).show();

                textview1.setText("Please type the verification code we sent \nto " + editText1.getText().toString().trim());
            }
        };

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = editText1.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(HomeActivity.this, "Please enter phone number...", Toast.LENGTH_SHORT).show();
                } else {
                    startPhoneNumberVerification(phone);
                }
            }
        });

        textview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = editText1.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(HomeActivity.this, "Please enter phone number...", Toast.LENGTH_SHORT).show();
                } else {
                    resendVerificationCode(phone, forceResendingToken);
                }
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editText2.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(HomeActivity.this, "Please enter verification code...", Toast.LENGTH_SHORT).show();
                } else {
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });
    }

    private void startPhoneNumberVerification(String phone) {
        pd.setMessage("Verifying Phone Number");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        pd.setMessage("Resending Code");
        pd.show();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        pd.setMessage("Verifying Code");
        pd.show();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        pd.setMessage("Logging In");
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        pd.dismiss();
                        String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(HomeActivity.this, "Logged In as " + phone, Toast.LENGTH_SHORT).show();

                        // Ajouter le docteur à la collection "docteurs"
                        addDoctorToFirestore(firebaseAuth.getCurrentUser().getUid(), phone);

                        // Rediriger vers la page des docteurs après ajout dans Firestore
                        startActivity(new Intent(HomeActivity.this, Doctors.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDoctorToFirestore(String userId, String phone) {
        // Référence à la collection "docteurs" dans Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Collection "docteurs"
        CollectionReference doctorsRef = db.collection("docteurs");

        // Créer les données du docteur
        Map<String, Object> doctorData = new HashMap<>();
        doctorData.put("userId", userId);  // ID de l'utilisateur (authentifié via téléphone)
        doctorData.put("phone", phone);  // Numéro de téléphone du docteur
        doctorData.put("username", "Dr. " + phone);  // Nom du docteur
        doctorData.put("specialty", "Cardiology");  // Spécialité du docteur
        doctorData.put("type", "docteur");  // Type de l'utilisateur (docteur)

        // Ajouter les données du docteur dans Firestore sous un nouveau document avec l'ID utilisateur
        doctorsRef.document(userId)  // Utilisation de l'ID de l'utilisateur comme ID du document
                .set(doctorData)  // Ajouter les informations du docteur
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Succès de l'ajout du docteur dans Firestore
                        Log.d(TAG, "Docteur ajouté à Firestore avec succès.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // En cas d'erreur lors de l'ajout dans Firestore
                        Log.e(TAG, "Erreur lors de l'ajout du docteur", e);
                        Toast.makeText(HomeActivity.this, "Erreur lors de l'ajout du docteur.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
