import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Test {
    private List<String> classes;
    private int totalClasses;
    
    private HashMap<String, int[]> vocabulary;
    
    private int totalWordsForClass[];
    private int totalTrainingDataForClass[];
    private int totalTrainingData;
    private double posterior[];

    private int totalAccuratelyClassifiedTestingData = 0;
    private int totalTestingData = 0;
        
    public Test(Train train) {
        classes = Utils.getClasses();
        totalClasses = classes.size();
        
        vocabulary = train.getVocabulary();
        totalWordsForClass = train.getTotalWordsForClass();
        totalTrainingDataForClass = train.getTotalTrainingDataForClass();
        totalTrainingData = train.getTotalTrainingData();
        
        posterior = new double[totalClasses];
    }
    
    public void classify() throws FileNotFoundException, IOException {
        
        for(String className : classes) {
            String subDirectoryLocation = Constants.DIRECTORY_LOCATION + "/" + className;
            File files[] = Utils.getFilesInDirectory(subDirectoryLocation);
            
            for(int fileIndex=1; fileIndex<files.length; fileIndex+=2) {
                String fileLocation = subDirectoryLocation + "/" + files[fileIndex].getName();
                List<String> words = Utils.getWordListFromFile(fileLocation);

                double logOfPrior[] = new double[totalClasses];
                double logOfLikelihood[] = new double[totalClasses];
                
                for(int classIndex=0; classIndex<totalClasses; classIndex++) {
                    logOfPrior[classIndex] = Math.log10((double)totalTrainingDataForClass[classIndex] / (double)totalTrainingData);
                }

                for(String word : words) {
                    if (Utils.isSeenWord(vocabulary, word)) {
                        int[] frequency = vocabulary.get(word);
                        
                        for(int classIndex=0; classIndex<totalClasses; classIndex++) {
                            if (frequency[classIndex] != 0) {
                                logOfLikelihood[classIndex] += Math.log10(((double)frequency[classIndex] / (double)totalWordsForClass[classIndex]));
                            }                        
                        }                         
                    }
                }
                
                for(int classIndex=0; classIndex<totalClasses; classIndex++) {
                    posterior[classIndex] = logOfPrior[classIndex] + logOfLikelihood[classIndex]; 
                }
                                
                normalizePosteriors();
                
                int targetClass = -1;
                double maxProbability = 0;
                
                for (int classIndex=0; classIndex<totalClasses; classIndex++) {
                    if (posterior[classIndex] > maxProbability) {
                        maxProbability = posterior[classIndex];
                        targetClass = classIndex;
                    }                    
                }
                
                String targetClassLabel = classes.get(targetClass);
                
                if (targetClassLabel.equals(className)) {
                    totalAccuratelyClassifiedTestingData++;
                }
                
                totalTestingData++;

                System.out.println("Testing#" + totalTestingData + ": " + targetClassLabel);
            }            
        }
    }
    
    private void normalizePosteriors() {
        double totalPosterior = 0;
                
        for(int classIndex=0; classIndex<totalClasses; classIndex++) {
            totalPosterior += posterior[classIndex];
        }
        
        for (int classIndex=0; classIndex<totalClasses; classIndex++) {
            posterior[classIndex] /= totalPosterior;
        }
    }
    
    public int getTotalAccuratelyClassifiedTestingData() {
        return totalAccuratelyClassifiedTestingData;
    }
    
    public int getTotalTestingData() {
        return totalTestingData; 
    }
}
