package com.HashTest;
/**
 * This class defines the hash objects contained
 * withing the hashtable
 * @author Kevin Haines
 * @param <E>
 */
public class HashObject<E> {
	private long frequency;
	private int classType;
	private E data;
	/**
	 * The main constructor for the HashObject class
	 * @param data
	 * @param classType 3 if is a String
	 */
	public HashObject(E data, int classType){
		this.data=data;
		frequency=0;
		this.classType=classType;
	}
	/**
	 * The empty contructor for the HashObject class
	 */
	public HashObject(){
		frequency=0;
		data=null;
		classType=0;
	}
	/**
	 * Getter method for the frequency variable
	 * @return frequency 
	 */
	public long getFrequency(){
		return frequency;
	}
	/**
	 * Getter method for the data object's key
	 * @return the hash code key for the data object
	 */
	public long getKey(){
		return 	data.hashCode();
	}
	/**
	 * Getter method for the data object
	 * @return data
	 */
	public E getData(){
		return data;
	}
	/**
	 * A method that cimple increments the frequency for
	 * the given hashobject by 1
	 */
	public void incrementFrequency(){
		frequency++;
	}
	@SuppressWarnings("unchecked")
	public boolean equals(Object hash){
		if(classType==3){
			String dataString = (String)data;
			HashObject<String> temp = (HashObject<String>)hash;
			String dataTwoString = (String)(temp.getData());

			return dataString.equals(dataTwoString);
		}
		else{
			HashObject<Long> temp = (HashObject<Long>)hash;
			return temp.getKey()==this.getKey();
		}
	}
	public String toString(){
		
		String result = "";
		if(classType==3)
			result = (String)data;
		else
			result = getKey()+"";
		
		return result;
		
	}
}
