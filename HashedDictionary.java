import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashedDictionary<K, V> implements DictionaryInterface<K, V> {
	private TableEntry<K, V>[] hashTable; 
	private int numberOfEntries;
	private int locationsUsed; 
	private static final int DEFAULT_SIZE = 2477; 
	private static final double MAX_LOAD_FACTOR = 0.5;
	private int CollisionCount = 0;

	public HashedDictionary() {
		this(DEFAULT_SIZE); 
	} 

	@SuppressWarnings("unchecked")
	public HashedDictionary(int tableSize) {
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntry[primeSize];
		numberOfEntries = 0;
		locationsUsed = 0;
	}

	public boolean isPrime(int num) {
		boolean prime = true;
		for (int i = 2; i <= num / 2; i++) {
			if ((num % i) == 0) {
				prime = false;
				break;
			}
		}
		return prime;
	}

	public int getNextPrime(int num) {
		if (num <= 1)
            return 2;
		else if(isPrime(num))
			return num;
        boolean found = false;   
        while (!found)
        {
            num++;     
            if (isPrime(num))
                found = true;
        }     
        return num;
	}

	public V add_hash(K key, V value,String Filename) {//Adding words and txt
		V oldValue; 
		if (isHashTableTooFull())
			rehash();
		String item=((Driver) value).getItem();
		int index = getHashIndex(key);
		index = probe(index,key,item);
		
		if ((hashTable[index] == null) || hashTable[index].isRemoved()) { 
			hashTable[index] = new TableEntry<K, V>(key, value);
			((Driver)hashTable[index].getValue()).AddTxtCount1(Filename);
			numberOfEntries++;
			locationsUsed++;
			oldValue = null;
		} else { 
			oldValue = hashTable[index].getValue();
			((Driver)hashTable[index].getValue()).AddTxtCount2(Filename);
		} 
		return oldValue;
	}
	public V add(K key, V value) {
		V oldValue; 
		if (isHashTableTooFull())
			rehash();
		String item=((Driver) value).getItem();
		int index = getHashIndex(key);
		index = probe(index,key,item);
		
		if ((hashTable[index] == null) || hashTable[index].isRemoved()) { 
			hashTable[index] = new TableEntry<K, V>(key, value);
			
			numberOfEntries++;
			locationsUsed++;
			oldValue = null;
		} else { 
			oldValue = hashTable[index].getValue();
			
		} 
		return oldValue;
	}

	public int getHashIndex(K key) {
		int hashIndex = key.hashCode() % hashTable.length;
		if (hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}

	public boolean isHashTableTooFull() {
		double load_factor = ((double) locationsUsed / (double) hashTable.length);
		if (load_factor >= MAX_LOAD_FACTOR)
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public void rehash() {
		TableEntry<K, V>[] oldTable = hashTable;
		int oldSize = hashTable.length;
		int newSize = getNextPrime(2 * oldSize);
		hashTable = new TableEntry[newSize]; 
		numberOfEntries = 0; 
		locationsUsed = 0;
		
		for (int index = 0; index < oldSize; index++) {
			if ((oldTable[index] != null) && oldTable[index].isIn())
				add(oldTable[index].getKey(), oldTable[index].getValue());
		}
	}

	private int probe(int index, K key,String item) {//This function checks key and word because some words have same key 
		boolean found = false;
		int removedStateIndex = -1;
		int i = 1;
		int trueIndex = index;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey())&&item.equals(((Driver) hashTable[index].getValue()).getItem()))
					found = true; 
				else {
					index = DoubleHashing(key, trueIndex, i)% hashTable.length; //Double Hashing
					//index = (index + 1) % hashTable.length;                   //Linear Probing
					while(index<0) {
						index=index+hashTable.length;
					}
					i++;
				}
			} 
			else 
			{
				if (removedStateIndex == -1)
					removedStateIndex = index;
				index = (index + 1) % hashTable.length; 
			} 
		} 
		if (found || (removedStateIndex == -1))
			return index; 
		else
			return removedStateIndex; 
	}
	
	private int DoubleHashing(K key, int index, int i) {//Calculating double hashing
		int result = i * (31-key.hashCode()%31);
		return result + index;
	}
	
	public int displayCollision() {
		return CollisionCount;
	}
	
	
	
	public V remove(K key) {
		V removedValue = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1) { 
			removedValue = hashTable[index].getValue();
			hashTable[index].setToRemoved();
			numberOfEntries--;
		} 
		return removedValue;
	}

	//Follows the probe sequence that begins at index (key’s hash index) and returns either the index
	//of the entry containing key or -1, if no such entry exists.
	private int locate(int index, K key) {
		boolean found = false;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey()))
					found = true; 
				else 
					index = (index + 1) % hashTable.length; 
			} 
		} 
		int result = -1;
		if (found)
			result = index;
		return result;
	}
	
	private int locateSearch(int index, K key,String word) {
		boolean found = false;
		int FailCollision = 0;
		int i = 1;//For counting the double hashing
		int trueIndex = index;
		while (!found && (hashTable[index] != null)) {
			if (hashTable[index].isIn()) {
				if (key.equals(hashTable[index].getKey())&& word.equals(((Driver) hashTable[index].getValue()).getItem()))
					found = true; 
				else {
					index =DoubleHashing(key, trueIndex, i)% hashTable.length;  //Double Hashing
					CollisionCount++;
					FailCollision++;
					//index = (index + 1) % hashTable.length;      //Linear Probing
					i++;
				}
					 
			} 
		}
		if (!found) {
			CollisionCount = CollisionCount - FailCollision; //If the word already is exist the hash table this is not count collision
		}
		int result = -1;
		if (found)
			result = index;
		return result;
	}

	@SuppressWarnings("unchecked")
	public V getValue(K key) {
		V result = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1)
			result =   hashTable[index].getValue(); 
		return result;
	}
	public V getValue(K key,String item) {
		V result = null;
		int index = getHashIndex(key);
		index = locateSearch(index, key,item);
		if (index != -1)
			result =   hashTable[index].getValue(); 
		return result;
	}

	public boolean contains(K key) {
		int index = getHashIndex(key);
		index = locate(index, key);
		if (index != -1)
			return true;
		return false;
	}
	
	public boolean search(K key, String word) {
		int index = getHashIndex(key);
		index =locateSearch(index, key, word);
		if (index != -1)
			return true;
		return false;
	}
	
	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public int getSize() {
		return numberOfEntries;
	}

	public void clear() {
		while(getKeyIterator().hasNext()) {
			remove(getKeyIterator().next());		
		}
	}
	
	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	private class TableEntry<S, T> {
		private S key;
		private T value;
		private boolean inTable;

		private TableEntry(S key, T value) {
			this.key = key;
			this.value = value;
			inTable = true;
		}

		private S getKey() {
			return key;
		}

		private T getValue() {
			return value;
		}

		private void setValue(T value) {
			this.value = value;
		}

		private boolean isRemoved() {
			return inTable == false;
		}

		private void setToRemoved() {
			inTable = false;
		}

		private void setToIn() {
			inTable = true;
		}

		private boolean isIn() {
			return inTable == true;
		}
	}

	private class KeyIterator implements Iterator<K> {
		private int currentIndex; 
		private int numberLeft; 

		private KeyIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		} 

		public boolean hasNext() {
			return numberLeft > 0;
		} 

		public K next() {
			K result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				} 
				result = hashTable[currentIndex].getKey();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		} 

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
	
	private class ValueIterator implements Iterator<V> {
		private int currentIndex; 
		private int numberLeft; 

		private ValueIterator() {
			currentIndex = 0;
			numberLeft = numberOfEntries;
		} 

		public boolean hasNext() {
			return numberLeft > 0;
		} 

		@SuppressWarnings("unchecked")
		public V next() {
			V result = null;
			if (hasNext()) {
				while ((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved()) {
					currentIndex++;
				} 
				result = (V) ((Driver) hashTable[currentIndex].getValue()).getItem();
				numberLeft--;
				currentIndex++;
			} else
				throw new NoSuchElementException();
			return result;
		} 

		public void remove() {
			throw new UnsupportedOperationException();
		} 
	}
}

