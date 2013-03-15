import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Client {
	private File corpus;
	private List<CorpusEntry> entries;
	
    public Client(File corpus) {
    	this.corpus = corpus;
    }
    
    public void loadCorpus() throws IOException {
    	this.entries = new ArrayList<CorpusEntry>();
    	for (File f : this.corpus.listFiles()) {
    		this.entries.add(new CorpusEntry(f));
    	}
    	
    	Collections.sort(this.entries,CorpusEntry.getDefaultComparator());
    }
    
    //Pull the corpus entry for a given query
    public CorpusEntry findCorpusEntry(String query) {
    	CorpusEntry search = new CorpusEntry(query);
    	int loc = Collections.binarySearch(this.entries, search, CorpusEntry.getDefaultComparator());
    	if (loc < 0) loc = -loc - 1; //Now the location points to the previous entry (that should hold hte value we're looking for)
    	return this.entries.get(loc);
    }
    
    public List<String> query(String query) throws IOException {
    	System.out.println("Executing Query: "+query);
    	
    	loadCorpus(); //TODO this should only be called once for many queries
    	CorpusEntry entry = findCorpusEntry(query);
    	entry.loadContent(); //TODO this should be done sparingly
    	
    	return entry.findMatchingContents(query);
    }

    public static void main(String[] args) throws IllegalArgumentException, IOException {
        if (args.length > 1 || args.length < 1) {
            throw new IllegalArgumentException("Provide a single search query!");
        }
        
        File corpus = new File("../processedCorpus");
        Client client = new Client(corpus);
        
        List<String> results = client.query(args[0]);
        for (String result : results) {
        	System.out.println("Result: "+result);
        }
    }
}
