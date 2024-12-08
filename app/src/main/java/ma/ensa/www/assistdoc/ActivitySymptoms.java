package ma.ensa.www.assistdoc;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySymptoms extends AppCompatActivity {

    Button dis;
    Spinner s1, s2, s3, s4, s5, s6, s7;
    String[] d = new String[7];

    // Utilisation de Map pour lier les maladies à leurs symptômes
    Map<String, List<String>> diseasesSymptoms = new HashMap<String, List<String>>() {
        {
            put("Diarrhoea", Arrays.asList("Stomach Ache", "Nausea", "Vomiting", "Fever", "Sudden Weight Loss"));
            put("Malaria", Arrays.asList("Fever", "Vomiting", "Sweating", "Muscle And Body Pain", "Headaches"));
            put("Typhoid", Arrays.asList("Fever", "Headaches", "Weakness/Fatigue", "Abdominal Pain", "Muscle Pain", "Dry Cough", "Diarrhoea/Constipation"));
            put("Diabetes", Arrays.asList("Frequent Urination", "Hunger", "Thirsty Than Usual", "Sudden Weight Loss", "Blurred Vision", "Skin Itching"));
            put("Blood Pressure", Arrays.asList("Headaches", "Blurred Vision", "Chest Pain", "Shortness in Breath", "Dizziness", "Nausea", "Vomiting"));
            put("Cardio Disease", Arrays.asList("Shortness in Breath", "Fast Heartbeat", "Indigestion", "Pressure Or Heaviness In Chest", "Anxiety"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        dis = findViewById(R.id.disease);
        s1 = findViewById(R.id.syp1);
        s2 = findViewById(R.id.syp2);
        s3 = findViewById(R.id.syp3);
        s4 = findViewById(R.id.syp4);
        s5 = findViewById(R.id.syp5);
        s6 = findViewById(R.id.syp6);
        s7 = findViewById(R.id.syp7);

        final String name = getIntent().getStringExtra("name");
        final String gender = getIntent().getStringExtra("gender");

        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Remplir les symptômes sélectionnés dans le tableau d[]
                d[0] = s1.getSelectedItem().toString();
                d[1] = s2.getSelectedItem().toString();
                d[2] = s3.getSelectedItem().toString();
                d[3] = s4.getSelectedItem().toString();
                d[4] = s5.getSelectedItem().toString();
                d[5] = s6.getSelectedItem().toString();
                d[6] = s7.getSelectedItem().toString();

                // Tableau pour compter les correspondances de symptômes
                int[] c = new int[6];

                // Parcourir chaque symptôme et l'associer à une maladie
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 6; j++) {
                        // Comparer avec les symptômes de chaque maladie
                        String diseaseName = (String) diseasesSymptoms.keySet().toArray()[j];
                        List<String> symptoms = diseasesSymptoms.get(diseaseName);
                        if (symptoms.contains(d[i])) {
                            c[j]++;
                        }
                    }
                }

                // Trouver la maladie avec le plus grand nombre de symptômes correspondants
                int maxIndex = 0;
                for (int i = 1; i < c.length; i++) {
                    if (c[i] > c[maxIndex]) {
                        maxIndex = i;
                    }
                }

                // Créer une nouvelle activité pour afficher le résultat
                Intent disPage = new Intent(ActivitySymptoms.this, ActivityDisease.class);
                disPage.putExtra("name", name);
                disPage.putExtra("gender", gender);
                disPage.putExtra("max", c[maxIndex]);
                disPage.putExtra("c", c);
                disPage.putExtra("disease", (String) diseasesSymptoms.keySet().toArray()[maxIndex]);
                startActivity(disPage);
            }
        });
    }
}
