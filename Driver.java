import java.util.Iterator;

public class Driver {
	public String item; 
	DictionaryInterface<String, Integer> TxtCount = new Dictionary<String, Integer>();
	public Driver(String item) {
		
		this.item=item;
		
	}
	
	public String getItem() {
		return item;
	}
	
	//Adding the txt, when the txt was not added before
	public void AddTxtCount1(String txt) {
		TxtCount.add(txt,1);
	}
	
	//Adding the txt, when the txt was added before
	public void AddTxtCount2(String txt) {
		//Different txt
		if (!(TxtCount.contains(txt)))
			TxtCount.add(txt,1);
		//Same txt
		else {	
			int newvalue=TxtCount.getValue(txt)+1;
			TxtCount.add(txt,newvalue);						
		}		
	}
	
	public DictionaryInterface<String, Integer> getTxtCount(){
		return TxtCount;
	}
	
	//Displaying the dictionary
    public void getTxtDisplay() {
		
        Iterator<String> keyIterator = TxtCount.getKeyIterator();
 		Iterator<Integer> valueIterator = TxtCount.getValueIterator();
 		while (keyIterator.hasNext()) {
 			System.out.println("Key: " + keyIterator.next() + " Value: " + valueIterator.next());
 		}			
	}
	

}
