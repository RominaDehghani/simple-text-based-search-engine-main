import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class mainCode {
	
	@SuppressWarnings("resource")
	public static void main(String[] args)  
	{
		//Creating Hash table
		HashedDictionary<Long, Driver> HashTable = new HashedDictionary<>();
        
        //Creating a string array for the names of the files
		String [] fileNumbers = {"001","002","003","004","005","006","007","008","009","010","011","012","013",
				"014","015","016","017","018","019","020","021","022","023","024","025","026","027","028","029",
				"030","031","032","033","034","035","036","037","038","039","040","041","042","043","044","045",
				"046","047","048","049","050","051","052","053","054","055","056","057","058","059","060","061",
				"062","063","064","065","066","067","068","069","070","071","072","073","074","075","076","077",
				"078","079","080","081","082","083","084","085","086","087","088","089","090","091","092","093",
				"094","095","096","097","098","099","100"};
		
		
		//--------Start Time-------//
		long startTime1 = System.nanoTime();
		//----------------------------------------------------------------------------------------------------------
		
		//Reading 100 files and Removing delimiters and Splitting the words of the files
		for (int i=0; i<fileNumbers.length; i++) {
			  
		    String FileName = fileNumbers[i] + ".txt";
			String data = null;
			try {
				data = new Scanner(new File(FileName)).useDelimiter("\\Z").next();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    String[] splitted = data.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
		      
		      
		    for (int j=0; j<splitted.length; j++) {
		    	
		    	//Calling the functions for finding keys
		        String word = splitted[j];
		        //Long SSFHash = (long) Simple_Summation_Function(word);
		        Long PAFHash = (long) Polynomial_Accumulation_Function(word);
				
			    Object item =new Driver(word);

			   //HashTable.add_hash(SSFHash,(Driver) item,FileName);
			   HashTable.add_hash(PAFHash,(Driver) item,FileName);
			   
	        }
				  
	    }
		
		//----------------------------------------------------------------------------------------------------------
		//-------End Time----------//
		long endTime1 = System.nanoTime();
		long duration1 = (endTime1 - startTime1)/1000000;
		//System.out.println("The Time is " + duration1);
		
		//------------------------------------------------Search--------------------------------------------//
		
		//------Start Time-------//
		long startTime2 = System.nanoTime();
		
		//Reading search file and Removing delimiters and Splitting the words of the file
		String data = null;
		try {
			data = new Scanner(new File("search.txt")).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] splitted = data.replaceAll("[^a-zA-Z ]", " ").toLowerCase().split("\\s+");
		
		//Calling the functions for finding keys
		for (int i=0; i<splitted.length; i++) {
		    String word = splitted[i];
		    //Long SSFHash = (long) Simple_Summation_Function(word);
		    Long PAFHash = (long) Polynomial_Accumulation_Function(word);
		        
			//HashTable.search(SSFHash, splitted[i]);
			HashTable.search(PAFHash, splitted[i]);
		}
		//----------------------------------------------------------------------------------------------------------
		
		//-------End Time-------//
		long endTime2 = System.nanoTime();
	    long duration2 = (endTime2 - startTime2)/1000000;
		//System.out.println("The Time is " + duration2);
		
	    //System.out.println(HashTable.displayCollision());
	    
	    //display(HashTable);
	    
	    
	   //----------------------------------------------------Input------------------------------------------------//
		
	    Get_Input(HashTable);
	    
	}
	//----------------------------------------------------------------------------------------------------------
	
	//Function for finding the simple keys
	public static long Simple_Summation_Function(String word) {
		long SSFHash = (long) 0;
		int n = word.length();	
		for (int k=0; k<n; k++) {
			char character = word.charAt(k);
			int ascii = (int) character;
			SSFHash = SSFHash + ascii;
		}
		return SSFHash;
	}
	
	//----------------------------------------------------------------------------------------------------------
	
	//Function for finding the polynomial keys
    public static long Polynomial_Accumulation_Function(String word) {
    	long PAFHash = (long) 0;
		int n = word.length();
		for (int k=0; k<n; k++) {
			char character = word.charAt(k);
			int ascii = (int) character;
			PAFHash = (long) ((int) (PAFHash + (Math.pow(3,n-1-k) * ascii)));
		}
		return PAFHash;
	}
    
    //----------------------------------------------------------------------------------------------------------
    
    //Displaying the Hash Table
	public static void display(HashedDictionary<Long,Driver> dataBase) {
		Iterator<Long> keyIterator = dataBase.getKeyIterator();
		Iterator<Driver> valueIterator = dataBase.getValueIterator();
		while (keyIterator.hasNext()) {
			System.out.println("Key: " + keyIterator.next() + " Value: " + valueIterator.next());
		}
	}
	
	//----------------------------------------------------------------------------------------------------------
	
	//Displaying the dictionary for txt and count
	public static void display1(DictionaryInterface<String, Integer> txtCountWord) {
		Iterator<String> keyIterator = txtCountWord.getKeyIterator();
		Iterator<Integer> valueIterator = txtCountWord.getValueIterator();
		while (keyIterator.hasNext()) {
			System.out.println("Key: " + keyIterator.next() + " Value: " + valueIterator.next());
		}
	}
	
	//----------------------------------------------------------------------------------------------------------
	
	//Finding the txt with most relevant count of word
	public static void Most_Relevant(String word1, String word2, String word3, HashedDictionary<Long, Driver> HashTable) {
		
		String [] fileNumbers = {"001","002","003","004","005","006","007","008","009","010","011","012","013",
				"014","015","016","017","018","019","020","021","022","023","024","025","026","027","028","029",
				"030","031","032","033","034","035","036","037","038","039","040","041","042","043","044","045",
				"046","047","048","049","050","051","052","053","054","055","056","057","058","059","060","061",
				"062","063","064","065","066","067","068","069","070","071","072","073","074","075","076","077",
				"078","079","080","081","082","083","084","085","086","087","088","089","090","091","092","093",
				"094","095","096","097","098","099","100"};
		
		Long PAFHash1 = (long) Polynomial_Accumulation_Function(word1);
		Long PAFHash2 = (long) Polynomial_Accumulation_Function(word2);
		Long PAFHash3 = (long) Polynomial_Accumulation_Function(word3);
		DictionaryInterface<String, Integer> TxtCountWord1= ((Driver) HashTable.getValue(PAFHash1, word1)).getTxtCount();
		DictionaryInterface<String, Integer> TxtCountWord2= ((Driver) HashTable.getValue(PAFHash2, word2)).getTxtCount();
		DictionaryInterface<String, Integer> TxtCountWord3= ((Driver) HashTable.getValue(PAFHash3, word3)).getTxtCount();
		
		int mostRelevantSum = 0;
		String mostRelevantTxt = "";
		
		for (int i=0; i<fileNumbers.length; i++) {
			int sum = 0;
			if(TxtCountWord1.contains(fileNumbers[i]+".txt")) {				
				sum=TxtCountWord1.getValue(fileNumbers[i]+".txt")+sum;
			}
			if(TxtCountWord2.contains(fileNumbers[i]+".txt")) {				
				sum=TxtCountWord2.getValue(fileNumbers[i]+".txt")+sum;
			}
			if(TxtCountWord3.contains(fileNumbers[i]+".txt")) {				
				sum=TxtCountWord3.getValue(fileNumbers[i]+".txt")+sum;
			}
			
			if (sum > mostRelevantSum) {
				mostRelevantSum = sum;
				mostRelevantTxt = fileNumbers[i];
			}
			
		}
		System.out.println("The most relevant txt is " + mostRelevantTxt + ".txt");
	}
	
	//----------------------------------------------------------------------------------------------------------
	
    //The function for getting input
	public static void Get_Input (HashedDictionary<Long, Driver> HashTable) {
		
		try (Scanner input = new Scanner(System.in)) {
			System.out.println("Please enter three word: ");
			String words = input.nextLine();
			String[] split=words.split(" ");
			String word1 = split[0];
			String word2 = split[1];
			String word3 = split[2];
			
			Most_Relevant(word1, word2, word3, HashTable);
		}
	}

}

