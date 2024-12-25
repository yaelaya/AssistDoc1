package ma.ensa.www.assistdoc;

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class WekaPrediction {

    public String predictDisease(Classifier classifier, Instances data, double age, String gender, String bloodPressure, String cholesterol, boolean fever, boolean cough, boolean fatigue, boolean difficultyBreathing) throws Exception {
        // Prepare new instance for prediction
        Instance instance = new DenseInstance(data.numAttributes());
        instance.setDataset(data);
        instance.setValue(data.attribute("Age"), age);
        instance.setValue(data.attribute("Gender"), gender);
        instance.setValue(data.attribute("Blood Pressure"), bloodPressure);
        instance.setValue(data.attribute("Cholesterol Level"), cholesterol);
        instance.setValue(data.attribute("Fever"), fever ? "Yes" : "No");
        instance.setValue(data.attribute("Cough"), cough ? "Yes" : "No");
        instance.setValue(data.attribute("Fatigue"), fatigue ? "Yes" : "No");
        instance.setValue(data.attribute("Difficulty Breathing"), difficultyBreathing ? "Yes" : "No");

        // Predict the disease
        double result = classifier.classifyInstance(instance);
        return data.classAttribute().value((int) result);
    }
}
