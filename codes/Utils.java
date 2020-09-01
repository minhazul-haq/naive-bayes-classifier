import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class Utils {
    
    public static List<String> getClasses() {
        File directory = new File(Constants.DIRECTORY_LOCATION);
        File subDirectories[] = directory.listFiles();
        List<String> classes = new ArrayList(); 

        for (File subDirectory : subDirectories) {
            classes.add(subDirectory.getName());
        }
        
        return classes;
    }
        
    public static File[] getFilesInDirectory(String directoryLocation) {
        File folder = new File(directoryLocation);
        
        return folder.listFiles();
    }
    
    public static List<String> getWordListFromFile(String fileLocation) throws FileNotFoundException, IOException {
        InputStream fileInputStream = new FileInputStream(fileLocation);
        InputStreamReader fileInputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader br = new BufferedReader(fileInputStreamReader);   
        String strLine;
        
        List<String> words = new ArrayList<>();
        
        while((strLine = br.readLine()) != null) {
            if(!strLine.toLowerCase().matches(Constants.LINE_IGNORING_PATTERN)) {
                StringTokenizer st = new StringTokenizer(strLine, Constants.DELIMS_TO_IGNORE);

                while(st.hasMoreTokens()) {  
                    String word = st.nextToken().toLowerCase();
                    words.add(word);
                }
            }
        }
        
        return words;
    }
        
    public static boolean isSeenWord(HashMap vocabulary, String word) {
        return (vocabulary.containsKey(word));
    }
    
    public static boolean isUnseenWord(HashMap vocabulary, String word) {
        return !(isSeenWord(vocabulary, word));
    }
}
