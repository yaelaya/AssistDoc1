package ma.ensa.www.assistdoc;

import android.content.res.AssetManager;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WekaDiseasePrediction {

    // Load the J48 model
    public static Classifier loadJ48Model(AssetManager assetManager, String modelFileName) {
        Classifier classifier = null;
        try (InputStream inputStream = assetManager.open(modelFileName);
             ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            classifier = (Classifier) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classifier;
    }

    public static String predictDisease(Classifier classifier, AssetManager assetManager, String arffFileName, Map<String, Object> inputData) {
        // Define attribute names and corresponding data types
        String[] attributeNames = {"Age", "Gender", "Symptoms"}; // Replace with your actual attribute names
        List<Double> values = new ArrayList<>();

        for (String attribute : attributeNames) {
            Object value = inputData.get(attribute);
            if (attribute.equals("Age")) {
                values.add(Double.parseDouble(value.toString())); // Assuming Age is numerical
            } else if (attribute.equals("Gender")) {
                // Assuming Gender is a string, treat "Male" as 0.0 and "Female" as 1.0
                values.add(value.toString().equalsIgnoreCase("Male") ? 0.0 : 1.0);
            } else if (attribute.equals("Symptoms")) {
                List<String> symptoms = (List<String>) value;
                double symptomsValue = calculateSymptomsValue(symptoms); // Convert symptoms to numerical values
                values.add(symptomsValue);
            }
        }

        try (InputStream arffInputStream = assetManager.open(arffFileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(arffInputStream))) {
            Instances instances = new Instances(reader);
            instances.setClassIndex(instances.numAttributes() - 1); // Assuming the last attribute is the class

            Instance instance = new DenseInstance(instances.numAttributes());
            instance.setDataset(instances);

            for (int i = 0; i < values.size(); i++) {
                instance.setValue(i, values.get(i));
            }

            return classifier.classifyInstance(instance) + ""; // Return the class prediction as a string
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Error in prediction"; // Return error message if something goes wrong
    }

    private static double calculateSymptomsValue(List<String> symptoms) {
        // Example logic to convert symptoms to numerical values
        // Implement your actual mapping logic here
        double value = 0.0;
        for (String symptom : symptoms) {
            switch (symptom) {
                case "Symptom1":
                    value += 1.0; // Add corresponding numerical value
                    break;
                case "Symptom2":
                    value += 2.0;
                    break;
                case "Symptom3":
                    value += 3.0;
                    break;
                case "Symptom4":
                    value += 4.0;
                    break;
                case "Symptom5":
                    value += 5.0;
                    break;
            }
        }
        return value;
    }
}
