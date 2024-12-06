package ma.ensa.www.assistdoc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ma.ensa.www.assistdoc.adapter.UserAdapter;
import ma.ensa.www.assistdoc.model.Users;

public class Doctors extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvUsers;
    private FirebaseRecyclerAdapter<Users, UserAdapter> adapter;
    private FirebaseRecyclerOptions<Users> options;
    private DatabaseReference ref;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        // Initialisation de drawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);  // Définir le listener
        // Référence Firebase
        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        Toast.makeText(this, "Firebase Reference Initialized", Toast.LENGTH_SHORT).show();

        // Initialisation du RecyclerView
        rvUsers = findViewById(R.id.recycler_users);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        // Création des options pour FirebaseRecyclerAdapter
        options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(ref, Users.class)
                .setLifecycleOwner(this)
                .build();

        // Création de l'adaptateur
        adapter = new FirebaseRecyclerAdapter<Users, UserAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserAdapter holder, int position, @NonNull Users model) {
                holder.usernameTextView.setText("Nom : " + model.getUsername());
                holder.emailTextView.setText("Email : " + model.getUseremail());
            }

            @NonNull
            @Override
            public UserAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
                return new UserAdapter(v);
            }

            @Override
            public void onError(@NonNull DatabaseError error) {
                Toast.makeText(Doctors.this, "Erreur Firebase : " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        // Démarrage de l'écoute
        rvUsers.setAdapter(adapter);



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, AskRole.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, DoctorProfile.class));
                break;
            case R.id.slots:
                startActivity(new Intent(this, Doctor_ChooseSlots.class));
                break;
            case R.id.appointment:
                startActivity(new Intent(this, Appointments.class));
                break;
            case R.id.chats:
                startActivity(new Intent(Doctors.this, Doctor_Chat_Display.class));
                break;
            case R.id.settingsApp:
                Intent settingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                settingsIntent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivity(settingsIntent);
                break;
            case R.id.website:
                openWebPage("https://www.example.com");
                break;
            case R.id.facebook:
                openWebPage("https://www.facebook.com/example");
                break;
            case R.id.twitter:
                openWebPage("https://www.twitter.com/example");
                break;
            case R.id.linkedin:
                openWebPage("https://www.linkedin.com/example");
                break;
            case R.id.logout:
                logoutUser();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openWebPage(String url) {
        if (url.isEmpty()) {
            Toast.makeText(this, "L'URL est vide", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    private void logoutUser() {
        Doctor_Session_Management sessionManagement = new Doctor_Session_Management(Doctors.this);
        sessionManagement.removeSession();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Doctors.this, AskRole.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
