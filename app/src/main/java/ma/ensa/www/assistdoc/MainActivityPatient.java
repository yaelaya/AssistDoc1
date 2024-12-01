package ma.ensa.www.assistdoc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityPatient extends AppCompatActivity {

    private Toolbar t;
    private DrawerLayout drawer;
    private EditText nametext, agetext;
    private ImageView enter, chatIcon;
    private RadioButton male, female;
    private RadioGroup rg;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_patient);

        // Initialisation des vues
        initializeViews();

        // Configuration de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configuration de la toolbar et du navigation drawer
        setupToolbarAndDrawer();

        // Récupération du NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Listener pour l'icône de chat
        chatIcon.setOnClickListener(view -> navController.navigate(R.id.chatFragment));

        // Listener pour le bouton "enter"
        enter.setOnClickListener(view -> navigateToSymptomsActivity());

        // Listener pour les items du NavigationView
        setupNavigationView(navController);
    }

    private void initializeViews() {
        drawer = findViewById(R.id.draw_activity);
        t = findViewById(R.id.toolbar);
        nametext = findViewById(R.id.nametext);
        agetext = findViewById(R.id.agetext);
        enter = findViewById(R.id.imageView7);
        chatIcon = findViewById(R.id.chat_icon);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        rg = findViewById(R.id.rg1);
    }

    private void setupToolbarAndDrawer() {
        setSupportActionBar(t);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupNavigationView(NavController navController) {
        NavigationView nav = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(nav, navController);

        nav.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_sos:
                    startActivity(new Intent(MainActivityPatient.this, call.class));
                    break;

                case R.id.nav_share:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_using)));
                    break;

                case R.id.nav_hosp:
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps/search/hospitals+near+me"));
                    startActivity(mapIntent);
                    break;

                case R.id.nav_cntus:
                    startActivity(new Intent(MainActivityPatient.this, ActivityContactUs.class));
                    break;

                case R.id.nav_medication:
                    startActivity(new Intent(MainActivityPatient.this, MainActivityMedi.class));
                    break;

                case R.id.logout:
                    signOut();
                    break;
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void navigateToSymptomsActivity() {
        String name = nametext.getText().toString().trim();
        String age = agetext.getText().toString().trim();
        String gender = "";

        int selectedId = rg.getCheckedRadioButtonId();
        if (selectedId == R.id.male) {
            gender = "Mr.";
        } else if (selectedId == R.id.female) {
            gender = "Ms.";
        }

        if (name.isEmpty() || age.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_all_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        Intent sympIntent = new Intent(MainActivityPatient.this, activity_symptoms.class);
        sympIntent.putExtra("name", name);
        sympIntent.putExtra("gender", gender);
        startActivity(sympIntent);
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(this, getString(R.string.logged_out_successfully), Toast.LENGTH_SHORT).show();

        Intent logoutIntent = new Intent(MainActivityPatient.this, activity_sign_in.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
    }
}
