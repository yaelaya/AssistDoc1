package ma.ensa.www.assistdoc;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.www.assistdoc.adapter.MedicationAdapter;
import ma.ensa.www.assistdoc.entities.Medicament;

public class MedicationListActivity extends AppCompatActivity {

    private Button buttonAddMedication;
    private RecyclerView recyclerView;
    private MedicationAdapter medicationAdapter;
    private SearchView searchView;
    private List<Medicament> allMedications; // Liste complète des médicaments

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        initializeViews();
        setupRecyclerView();
        setupSearchView();

        // Initialisation Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Récupérer l'UID de l'utilisateur authentifié
        String userId = mAuth.getCurrentUser().getUid();

        // Récupérer les médicaments de Firestore
        fetchMedicationsFromFirestore(userId);

        // Gestion du bouton pour ajouter un médicament
        buttonAddMedication.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivityMedi.class);
            startActivity(intent);
        });
    }

    private void initializeViews() {
        buttonAddMedication = findViewById(R.id.buttonAddMedication);
        recyclerView = findViewById(R.id.recyclerViewMeds);
        searchView = findViewById(R.id.search_button);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicationAdapter = new MedicationAdapter(new ArrayList<>(), position -> confirmDelete(position));
        recyclerView.setAdapter(medicationAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (allMedications != null) {
                    filterMedications(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (allMedications != null) {
                    filterMedications(newText);
                }
                return true;
            }
        });
    }

    private void filterMedications(String query) {
        List<Medicament> filteredList = new ArrayList<>();
        for (Medicament medicament : allMedications) {
            if (medicament.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(medicament);
            }
        }
        medicationAdapter.updateList(filteredList);
    }

    private void fetchMedicationsFromFirestore(String userId) {
        // Récupérer les médicaments de la sous-collection "medicaments" sous l'utilisateur
        db.collection("Users").document(userId).collection("medications")
                .orderBy("nom", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Medicament> medications = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Medicament medicament = document.toObject(Medicament.class);
                            medicament.setId(document.getId());  // Set the document ID to the Medicament object
                            medications.add(medicament);
                        }
                        allMedications = medications;
                        medicationAdapter.updateList(medications);
                    } else {
                        Toast.makeText(MedicationListActivity.this, "Erreur lors de la récupération des médicaments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void confirmDelete(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation de suppression")
                .setMessage("Voulez-vous vraiment supprimer ce médicament ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    Medicament medicamentToDelete = medicationAdapter.getMedications().get(position);
                    deleteMedicament(medicamentToDelete);
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteMedicament(Medicament medicament) {
        // Supprimer le médicament dans Firestore
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("Users").document(userId)
                .collection("medicaments")
                .document(medicament.getId()) // Use the ID as the argument
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Médicament supprimé", Toast.LENGTH_SHORT).show();
                    fetchMedicationsFromFirestore(userId); // Recharger la liste après suppression
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur lors de la suppression du médicament", Toast.LENGTH_SHORT).show();
                });
    }
}
