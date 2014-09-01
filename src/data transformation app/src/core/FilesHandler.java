package core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesHandler {

	public static final double NORMALIZING_FACTOR = -10000.0;
	public static final String URL_PREFIX = "http://";
	public static final String URL_PREFIX_S = "https://";
	public static final String URL_PREFIX_WWW = "www.";
	public static final String SLASH = "/";

	public static final String EMPTY_STRING = "";
	
	public static Comparator<Row> websiteRowComparator = new Comparator<Row>() {
		
		@Override
		public int compare(Row o1, Row o2) {
			return o1.website.compareTo(o2.website);
		}
	};

	/***
	 * Compares two Strings based on their length.
	 */
	public static Comparator<String> StringLengthComparator = new Comparator<String>() {
		@Override
		public int compare(String a, String b) {
			int lenghtA = a.length();
			int lengthB = b.length();
			return (new Integer(lengthB)).compareTo(new Integer(lenghtA));
		}
	};

	/***
	 * Returns the website address out of a URL, given as parameter. The address 
	 * returned does not include the "https" suffix.
	 * @param url
	 * @return
	 */
	public String extractWebsiteAddress(String url) {
		// remove http/https
		String websiteAddress = url;
		websiteAddress = websiteAddress.replace(FilesHandler.URL_PREFIX,
				FilesHandler.EMPTY_STRING);
		websiteAddress = websiteAddress.replace(FilesHandler.URL_PREFIX_S,
				FilesHandler.EMPTY_STRING);
		websiteAddress = websiteAddress.replace(FilesHandler.URL_PREFIX_WWW,
				FilesHandler.EMPTY_STRING);

		// remove the url info, after the wite address
		if (websiteAddress.contains(FilesHandler.SLASH)) {
			int slashIndex = websiteAddress.indexOf(FilesHandler.SLASH);
			websiteAddress = websiteAddress.substring(0, slashIndex);
		}

		if (websiteAddress.endsWith("/")) {
			websiteAddress.replace("/", "");
		}

		return websiteAddress;
	}

	/***
	 * Returns the domain name out of a website address, given as parameter
	 * @param websiteAddress
	 * @return
	 */
	public String extractDomainName(String websiteAddress) {
		final String domainNameSoFar = websiteAddress;
		
		String domainSuffix = this.getAllDomainSuffixes().stream()
				.filter(suffix -> domainNameSoFar.endsWith(suffix))
				.sorted(StringLengthComparator).findFirst()
				.orElse(FilesHandler.EMPTY_STRING);

		if (domainSuffix != null) {
			websiteAddress = websiteAddress.replace(domainSuffix, "");
		}

		String domainName = null;
//		System.out.println("In extractDomainName: websiteAddress = " + websiteAddress);
		if(this.isIPAddress(websiteAddress)) {
			domainName = websiteAddress;
//			System.err.println("In extractDomainName: websiteAddress = " + websiteAddress);
		} 
		else if(websiteAddress.contains(".")){
			
			//String[] domainCandidates = websiteAddress.split(".");
			//System.out.println("In extractDomainName: domainCandidates length = " + domainCandidates.length);
			
			//domainName = domainCandidates[Math.max(domainCandidates.length - 1,0)];
			domainName = websiteAddress.substring(websiteAddress.lastIndexOf(".") + 1, websiteAddress.length());
		}
		else
		{
			domainName = websiteAddress;
		}
//		System.out.println("In extractDomainName: domainName = " + domainName);
		return domainName;
	}

	/***
	 * 
	 */
	private ArrayList<String> allDomainSuffixes;

	/***
	 * 
	 * @return
	 */
	public ArrayList<String> getAllDomainSuffixes() {
		try {
			
			if (this.allDomainSuffixes == null
					|| this.allDomainSuffixes.isEmpty()) {
				allDomainSuffixes = new ArrayList<String>();
				BufferedReader reader;

				reader = new BufferedReader(
						new FileReader(
								"C:\\temp\\allDomainSuffixes.txt"));

				String line = null;
				while (true) {
					line = reader.readLine();
					if (line == null) {
						break;
					}

					if (!line.startsWith(".")) {
						continue;
					}

					allDomainSuffixes.add(line.split("\t")[0]);

				}
				reader.close();
			}
			
			return allDomainSuffixes;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	
	public void writeToFile(String domainName, String webSiteAddress, String scores)
	{
		try {
			String filename = null;
			if(domainName.length() < 2){
				return;//filename = domainName.substring(0, 1);
			}
			else{	
				filename = domainName.substring(0, 2);
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\temp\\intermediate_files\\"+ filename), true));
			
			writer.write(domainName + "\t" + webSiteAddress + "\t" + scores + "\n\r");
			
			writer.flush();
			writer.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void readFromFile(String filepath){
		try {
//			System.out.println("In readFromFile");
			BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
			
			String line = null, newLine = null;
			String url = null, domainName = null, websiteAddress = null;  
			
			line = reader.readLine();
			int counter = 0;
			while(true){
				
				counter ++;
				if(counter % 10000 == 0){
					System.out.println("Read " + counter + " lines so far...");
				}
					
				
				// read each line
				line = reader.readLine();
				
				if(line == null){
					break;
				}
				
				if(line.length() == 0){
					continue;
				}
				
//				System.out.println("In readFromFile: line = " + line);
				
				// isolate teh url
				url = line.split("\t")[0];
				
				// remove it form the line
				newLine = line.replace(url+"\t",	"");
				
				// extract the webaddress and teh domain name
				websiteAddress = this.extractWebsiteAddress(url);
				domainName =  this.extractDomainName(websiteAddress);
				
				// write to file
				this.writeToFile(domainName, websiteAddress, newLine);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Collection<File> listFileTree(File dir) {
	    Set<File> fileTree = new HashSet<File>();
	    for (File entry : dir.listFiles()) {
	        if (entry.isFile()) fileTree.add(entry);
	        else fileTree.addAll(listFileTree(entry));
	    }
	    return fileTree;
	}
	
	public void handleFiles(String directoryName){
		try {
//		System.out.println("In handleFiles: directoryName = " + directoryName);
		File directory = new File(directoryName);
		//String[] filenames = directory.list();
		Collection<File> files = this.listFileTree(directory);
		
		
//		System.out.println("In handleFiles: filenames length = " + filenames.length);
		
		int i = 0;
		for(File file : files) {
		//for(String filename : filenames) {
			
//			if(filename.equals("allDomainSuffixes.txt"))
//				continue;
//			
//			if(!filename.endsWith(".tsv"))
//				continue;
			
			System.out.println("In handleFiles: filename i = " + file.getName());//filename);
			System.out.println("In handleFiles: filename i = " + file.getAbsolutePath());//directory.getAbsolutePath() + "\\" + filename);
			
			this.readFromFile(file.getCanonicalPath());
			i++;
			
			
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//directory.getAbsolutePath() + "\\" + filename);
	}
	
	
	
	private static final String IP_PATTERN = 
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public static boolean isIPAddress(final String ip){          

	      Pattern pattern = Pattern.compile(IP_PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
	
	public void sortAllFilesContents(String directoryName){
		
		File directory = new File(directoryName);
		String[] filenames = directory.list();
		
		
		
//		System.out.println("In handleFiles: filenames length = " + filenames.length);
		
		int i = 0;
		for(String filename : filenames) {
			this.sortFileContent(directoryName, filename);
			i++;
			if(i%1000 == 0)
				System.out.println("Files sorted so far: " + i);
		}
	}
	
	//// sort
	public void sortFileContent(String dirname, String filename){
		try {
		System.out.println("sorting file: "+filename+ "...");
		BufferedReader reader = new BufferedReader(new FileReader(new File(dirname + "\\" + filename)));
		ArrayList<Row> rows = new ArrayList<Row>();
		
		String line = null, newLine = null;
		String url = null, domainName = null, websiteAddress = null;  
		
		// read
		line = reader.readLine();
		int counter = 0;
		while(true){
			
			// read each line
			line = reader.readLine();
			
			if(line == null){
				break;
			}
			
			if(line.trim().length() == 0){
				continue;
			}
			
			
			rows.add(new Row(line));
			//System.out.println(rows.last());
			
		}
		reader.close();
		reader=null;
		
		// write
		ArrayList<Row> alike = new ArrayList<Row>();
		ArrayList<Row> newRows = new ArrayList<Row>();
		String currentDomain = null, currentWebsite = null;
		Row newRow;
		
		rows.sort(websiteRowComparator);
		
		System.out.println(" -- rows size: "+rows.size());
		while (!rows.isEmpty()){
			newRow = null;
			alike.clear();
						
			currentDomain = rows.get(0).domain;
			currentWebsite = rows.get(0).website;
			alike.clear();
			
			for(Row r : rows){
				if(r.website.equals(currentWebsite))
					alike.add(r);
			}
			
			rows.removeAll(alike);
//			if(Math.random() > 0.8){
//			System.out.println(" -- Random info: (iteration over rows) rows size: "+rows.size());
//			System.out.println(" -- Random info: (iteration over rows) alike size: "+alike.size());
//			}
			
			Row tmp;
			int alikeAmount = alike.size();
			while (!alike.isEmpty()){
				if(newRow == null)
					newRow = new Row(currentDomain, currentWebsite);
				
				tmp = alike.remove(0);
				String tmpCategory; double tmpScore;
				for(int ai=0; ai<tmp.categories.size(); ai++){
					tmpCategory = tmp.categories.get(ai);
					tmpScore = tmp.scores.get(ai);
					
					if(newRow.categories.contains(tmpCategory)){
						int index = newRow.categories.indexOf(tmpCategory);
						newRow.scores.set(index, newRow.scores.get(index)+tmpScore);
					}
					else
					{
						newRow.categories.add(tmpCategory);
						newRow.scores.add(tmpScore);
					}
				}
//				System.out.println(" --(iteration over alike) alike size: "+alike.size());
			}
			
			
			
			for(int f=0;f<newRow.scores.size();f++){
				newRow.scores.set(f, newRow.scores.get(f)/alikeAmount);
			}
			newRows.add(newRow);
			
		}
		
		// write
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\temp\\final_files\\"+filename), true));
		for(Row r : newRows)
		{
			writer.write(r.domain + "\t" + r.website);
			for(int g=0;g<r.categories.size();g++)
			{
				writer.write( "\t" + r.categories.get(g) + ":" + r.scores.get(g));
			}
			writer.write("\r\n");
		}
		writer.flush();
		writer.close();
		
	
	
		System.out.println("sorted file: "+filename);
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
}


