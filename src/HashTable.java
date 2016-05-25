package com.HashTest;
import java.util.Random;
import java.util.ArrayList;

/**
 * This class defines the HashTable data type.
 * The constructor adds elements into the table
 * as specified by the arguments and uses various
 * helper methods.
 * @author Kevin Haines
 */
@SuppressWarnings("unused")
public class HashTable {
	private HashObject<?>[] table;
	private int n, m, fullN, probeCount, count;
	private ArrayList<String> wordList;
	/**
	 * This constructor creates and populates the HashTable object
	 * in accordance to the values in the arguments
	 * @param linOrDouble 0 indicates linear probing and 1 indicates double hashing
	 * @param size the size of the table
	 * @param dataSource 1 indicates Random class,2 System.nanoTime(), and 3 from file word-list 
	 * @param loadFactor n/m
	 * @param debug the debug level
	 * @param wordList ArrayList containing all the words in the word-list file
	 */
	public HashTable(int linOrDouble, int size, int dataSource, double loadFactor, int debug, ArrayList<String> wordList) {
		this.wordList=wordList;
		m=size;
		table = new HashObject[m];
		n=0;
		probeCount=0;
		fullN=(int)(m*loadFactor);
		if(m*loadFactor-fullN!=0)
			fullN++;

		int count2=0;
		if(debug==0){
			if(linOrDouble==0)
				System.out.println("Using Linear Hashing...");
			else
				System.out.println("Using Double Hashing...");
		}
		if(debug==2){
			if(linOrDouble==0)
				System.out.println("Linear Hashing Table: Probes per insert:");
			else
				System.out.println("Double Hashing Table: Probes per insert:");

		}
		for(int i=0;n!=fullN;i++){
			long nextKey=0;
			boolean isDup = false;
			count=0;
			HashObject<?> tempHash;
			Random rand = new Random();
			if(dataSource==1){
				nextKey = (long) rand.nextInt();
				if(nextKey<0)
					nextKey *=-1;
				tempHash = new HashObject<Long>(nextKey, dataSource);
			}
			else if(dataSource==2){
				nextKey = System.nanoTime();
				nextKey= Math.abs(nextKey);
				tempHash = new HashObject<Long>(nextKey, dataSource);
			}
			else {
				String next = wordList.get(count2);
				nextKey = next.hashCode();
				if(nextKey<0)
					nextKey *=-1;
				tempHash = new HashObject<String>(next, dataSource);
				count2++;
			}
			if(linOrDouble==0){
				while(table[linDivMethod(nextKey,count)]!=null){
					if(table[linDivMethod(nextKey,count)].equals(tempHash)){
						table[linDivMethod(nextKey,count)].incrementFrequency();
						isDup=true;
					}
					count++;
				}
				if(!isDup){
					table[linDivMethod(nextKey,count)]= tempHash;
					count++;
					probeCount+=count;
					n++;
				}
			}
			else{
				while(table[doubDivMethod(nextKey,count)]!=null ){
					if(table[doubDivMethod(nextKey,count)].equals(tempHash)){
						table[doubDivMethod(nextKey,count)].incrementFrequency();
						isDup=true;
					}
					count++;
				}
				if(!isDup){
					table[doubDivMethod(nextKey,count)]= tempHash;
					count++;
					if(debug==2)
						System.out.println("table["+doubDivMethod(nextKey,count)+"] Number of probes: "+count);
					probeCount+=count;
					n++;
				}
			}
		}
		if(debug==0){

			System.out.println("Inserted "+n+", of which "+getDuplicates()+" duplicates");
			System.out.println("load factor = "+loadFactor+" Avg. no. of probes "+((double)probeCount/(double)n)+"\n");
		}

	}

	/**
	 * This method defines the linear hashing function
	 * @param key
	 * @param i
	 * @return the result of the linear hashing function
	 */
	private int linDivMethod(long key , int i){
		return (int)(key%m+i)%m;
	}
	/**
	 * This method defines the double hashing function
	 * @param key
	 * @param i
	 * @return the result of the double hashing function
	 */
	private int doubDivMethod(long key , int i){
		return (int)(key%m+i*(1+(key%(m-2))))%m;
	}

	/**
	 * @return n, the number of elements in the Table
	 */
	public int getN(){
		return n;
	}

	/**
	 * @return probeCount number of probes
	 */
	public int getProbeCount(){
		return probeCount;
	}
	/**
	 * @param num index of the table to get the object
	 * @return the hashObject at the specified table index
	 */
	public HashObject<?> getObject(int num){
		return table[num];
	}
	/**
	 * @return the number of objects added to the table that have more
	 * than one instance of itself.
	 */
	public int getDuplicates(){
		int dupCount=0;
		for(int i=0; i<n;i++){
			if(table[i]!=null)
				dupCount+=table[i].getFrequency();
		}
		return dupCount;
	}

}
