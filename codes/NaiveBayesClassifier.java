import java.io.FileNotFoundException;
import java.io.IOException;

public class NaiveBayesClassifier {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Train train = new Train();
        train.learn();

        Test test = new Test(train);
        test.classify();
        
        double accuracy = (double)test.getTotalAccuratelyClassifiedTestingData() / (double)test.getTotalTestingData();
        System.out.format("\nClassification accuracy: %.2f%%", accuracy*100);
    }
}
