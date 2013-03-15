import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class CorpusEntry {
	private File file;
	private int size;
	private String first,last;
	private List<String> contents;
	
	//10 aardvark aztec
	public CorpusEntry(File f) throws IOException {
		String line = getFirstLineOfFile(f);
		
		String[] split = line.split(",");
		this.file = f;
		this.size = Integer.parseInt(split[0]);
		this.first = split[1];
		this.last = split[2];
	}
	
	// This is sorta a hacky constructor that will be used for binary search
	public CorpusEntry(String s) {
		this.first = s;
	}
	
	private String getFirstLineOfFile(File f) throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader(f));
    	String line = br.readLine();
    	br.close();
    	return line;
	}
	
	//Loads the entire file into memory
	public void loadContent() throws IOException {
    	BufferedReader br = new BufferedReader(new FileReader(this.file));
    	String line;
    	
    	contents = new ArrayList<String>(size);
    	while ( (line = br.readLine()) != null ) {
    		contents.add(line);
    	}
    	br.close();
	}

	public int getSize() {
		return size;
	}

	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}

	public static Comparator<CorpusEntry> getDefaultComparator() {
		return new Comparator<CorpusEntry>() {
			@Override
			public int compare(CorpusEntry o1, CorpusEntry o2) {
				return o1.first.compareTo(o2.first);
			}
		};
	}

	public List<String> findMatchingContents(String query) {
		int loc = Collections.binarySearch(contents, query);
		if (loc < 0) loc = -loc;
		
		List<String> matches = new LinkedList<String>();
		while(loc < contents.size() && contents.get(loc).startsWith(query) ) {
			matches.add(contents.get(loc));
		}
		
		return matches;
	}
}