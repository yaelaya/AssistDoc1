package ma.ensa.www.assistdoc;

import java.io.InputStream;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class WekaTraining {

    public Classifier trainClassifier(String arffFileName) throws Exception {
        // Load ARFF file from assets
        InputStream arffStream = null;
        try {
            arffStream = getClass().getClassLoader().getResourceAsStream("assets/" + arffFileName);
            if (arffStream == null) {
                throw new IOException("ARFF file not found in assets: " + arffFileName);
            }

            DataSource source = new DataSource(arffStream);
            Instances data = source.getDataSet();

            // Set the class index (Disease attribute)
            if (data.classIndex() == -1) {
                data.setClassIndex(0); // Assuming the 'Disease' attribute is the first attribute
            }

            // Train a classifier (e.g., J48 Decision Tree)
            Classifier classifier = new J48();
            classifier.buildClassifier(data);

            return classifier;

        } finally {
            if (arffStream != null) {
                arffStream.close(); // Close the InputStream to release resources
            }
        }
    }
}
