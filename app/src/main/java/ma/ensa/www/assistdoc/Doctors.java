package ma.ensa.www.assistdoc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.www.assistdoc.adapter.UserAdapter;
import ma.ensa.www.assistdoc.model.Users;

public class Doctors extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvUsers;
    private UserAdapter userAdapter;
    private List<Users> usersList;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        // Initialiser le DrawerLayout et la NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);  // Définir le listener

        // Initialiser la RecyclerView
        rvUsers = findViewById(R.id.recycler_users);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();

        // Récupérer les utilisateurs depuis Firebase
        fetchUsersFromFirebase();
    }

    private void fetchUsersFromFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Réinitialiser la liste avant d'ajouter de nouveaux éléments
                    usersList.clear();

                    // Parcours de chaque utilisateur dans la base de données
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Users user = dataSnapshot.getValue(Users.class);

                        // S'assurer que l'utilisateur n'est pas nul
                        if (user != null) {
                            usersList.add(user);
                        }
                    }

                    // Vérifier si l'adaptateur existe déjà, sinon l'initialiser
                    if (userAdapter == null) {
                        userAdapter = new UserAdapter(usersList, Doctors.this);
                        rvUsers.setAdapter(userAdapter);
                    } else {
                        // Si l'adaptateur existe déjà, notifier qu'il y a des changements
                        userAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(Doctors.this, "No users found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Afficher un message d'erreur si la récupération échoue
                Toast.makeText(Doctors.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, AskRole.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, Doctors_View_Profile.class));
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
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:"+getApplicationContext().getPackageName()));
                startActivity(intent);
                break;
            case R.id.website:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("")));
                break;
            case R.id.facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("")));
                break;
            case R.id.twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("")));
                break;
            case R.id.linkedin:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("")));
                break;
            case R.id.logout:
                Doctor_Session_Management doctors_session_mangement = new Doctor_Session_Management(Doctors.this);
                doctors_session_mangement.removeSession();
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(Doctors.this, AskRole.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
