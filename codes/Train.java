import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Train {
    private List<String> classes;
    private int totalClasses;
    
    private HashMap<String, int[]> vocabulary = new HashMap<>();
    
    private int totalWordsForClass[];
    private int totalTrainingDataForClass[];
    private int totalTrainingData = 0;
    
    public Train() {
        classes = Utils.getClasses();
        totalClasses = classes.size();
        
        totalWordsForClass = new int[totalClasses];
        totalTrainingDataForClass = new int[totalClasses];
    }
    
    public void learn() throws FileNotFoundException, IOException {
        
        for(String className : classes) {
            String subDirectoryLocation = Constants.DIRECTORY_LOCATION + "/" + className;
            File[] files = Utils.getFilesInDirectory(subDirectoryLocation);

            int classIndex = classes.indexOf(className);
            
            for(int fileIndex=0; fileIndex<files.length; fileIndex+=2) {
                String fileLocation = subDirectoryLocation + "/" + files[fileIndex].getName();    
                List<String> words = Utils.getWordListFromFile(fileLocation);
                
                for(String word : words) {
                    if (Utils.isUnseenWord(vocabulary, word)) {
                        vocabulary.put(word, new int[totalClasses]);
                    }
                    
                    int[] frequency = vocabulary.get(word);
                    frequency[classIndex]++;

                    vocabulary.put(word, frequency);
                }
                
                totalTrainingDataForClass[classIndex]++;
                totalTrainingData++;
                
                System.out.println("Training#" + totalTrainingData);
            }            
        }
        
        removeMostFrequentWords();
        calculateTotalWordsPerClass();
    }
    
    private void removeMostFrequentWords() {
        System.out.println("Removing most frequent words...");
        
        for(int i=0; i<Constants.TOTAL_MAX_FREQUENT_WORDS_TO_REMOVE; i++) {
            int maxFrequency = 0;
            String keyWithMaxFrequency = new String();
            
            for (Map.Entry<String, int[]> entry : vocabulary.entrySet()) {
                int[] frequency = entry.getValue();
                int totalFrequency = 0;
                
                for(int j=0; j<totalClasses; j++) {
                    totalFrequency += frequency[j];
                }
                
                if (totalFrequency > maxFrequency) {
                    maxFrequency = totalFrequency;
                    keyWithMaxFrequency = entry.getKey();
                }
            }
            
            vocabulary.remove(keyWithMaxFrequency);
        }
    }
    
    private void calculateTotalWordsPerClass() {
        for (int[] frequency : vocabulary.values()) {
            for (int classIndex=0; classIndex<totalClasses; classIndex++) {
                totalWordsForClass[classIndex] += frequency[classIndex];
            }
        }
    }
    
    public HashMap<String, int[]> getVocabulary() {
        return vocabulary;
    }
    
    public int[] getTotalWordsForClass() {
        return totalWordsForClass;
    }
    
    public int[] getTotalTrainingDataForClass() {
        return totalTrainingDataForClass;
    }
    
    public int getTotalTrainingData() {
        return totalTrainingData; 
    }
}
