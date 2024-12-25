package ma.ensa.www.assistdoc;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.InputStream;

public class WekaMain extends AppCompatActivity {

    private EditText edtAge;
    private RadioGroup rgGender;
    private Button btnPredict;
    private CheckBox chkSymptom1, chkSymptom2, chkSymptom3, chkSymptom4;
    private Spinner spinnerBloodPressure, spinnerCholesterol;
    private TextView tvPredictedDisease;

    private Classifier classifier;
    private Instances data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_disease);

        // Initialize UI components
        edtAge = findViewById(R.id.edtAge);
        rgGender = findViewById(R.id.rgGender);
        btnPredict = findViewById(R.id.btnPredict);
        chkSymptom1 = findViewById(R.id.chkFever);
        chkSymptom2 = findViewById(R.id.chkCough);
        chkSymptom3 = findViewById(R.id.chkFatigue);
        chkSymptom4 = findViewById(R.id.chkDifficultyBreathing);
        spinnerBloodPressure = findViewById(R.id.spinnerBloodPressure);
        spinnerCholesterol = findViewById(R.id.spinnerCholesterol);
        tvPredictedDisease = findViewById(R.id.tvPredictedDisease);

        // Set up spinners
        ArrayAdapter<CharSequence> bpAdapter = ArrayAdapter.createFromResource(
                this, R.array.blood_pressure_levels, android.R.layout.simple_spinner_item);
        bpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodPressure.setAdapter(bpAdapter);

        ArrayAdapter<CharSequence> cholAdapter = ArrayAdapter.createFromResource(
                this, R.array.cholesterol_levels, android.R.layout.simple_spinner_item);
        cholAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCholesterol.setAdapter(cholAdapter);

        // Load model and dataset
        loadModelAndDataset();

        // Set predict button listener
        btnPredict.setOnClickListener(this::onPredictButtonClick);
    }

    private void loadModelAndDataset() {
        try {
            AssetManager assetManager = getAssets();

            // Load ARFF file
            InputStream arffStream = assetManager.open("table_1.arff");
            DataSource source = new DataSource(arffStream);
            data = source.getDataSet();

            // Set class index
            if (data.classIndex() == -1) {
                data.setClassIndex(0); // Assuming 'Disease' is the first attribute
            }

            // Train classifier
            classifier = new J48();
            classifier.buildClassifier(data);

        } catch (Exception e) {
            Log.e("WekaMain", "Error loading model or dataset", e);
        }
    }

    private void onPredictButtonClick(View view) {
        try {
            // Gather user input
            double age = Double.parseDouble(edtAge.getText().toString());
            String gender = (rgGender.getCheckedRadioButtonId() == R.id.rbMale) ? "Male" : "Female";
            String bp = spinnerBloodPressure.getSelectedItem().toString();
            String chol = spinnerCholesterol.getSelectedItem().toString();

            // Prepare new instance
            Instance instance = new DenseInstance(data.numAttributes());
            instance.setDataset(data);
            instance.setValue(data.attribute("Age"), age);
            instance.setValue(data.attribute("Gender"), gender);
            instance.setValue(data.attribute("Blood Pressure"), bp);
            instance.setValue(data.attribute("Cholesterol Level"), chol);
            instance.setValue(data.attribute("Fever"), chkSymptom1.isChecked() ? "Yes" : "No");
            instance.setValue(data.attribute("Cough"), chkSymptom2.isChecked() ? "Yes" : "No");
            instance.setValue(data.attribute("Fatigue"), chkSymptom3.isChecked() ? "Yes" : "No");
            instance.setValue(data.attribute("Difficulty Breathing"), chkSymptom4.isChecked() ? "Yes" : "No");

            // Predict disease
            double result = classifier.classifyInstance(instance);
            String predictedDisease = data.classAttribute().value((int) result);

            // Display result
            tvPredictedDisease.setText("Predicted Disease: " + predictedDisease);
            tvPredictedDisease.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            Log.e("WekaMain", "Error predicting disease", e);
            tvPredictedDisease.setText("Error predicting disease. Please check input.");
            tvPredictedDisease.setVisibility(View.VISIBLE);
        }
    }
}
