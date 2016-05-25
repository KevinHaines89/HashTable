package com.HashTest;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * This class accepts 2 or 3 command line arguments
 * which determine how the program will run.
 * Command args:        <input type> <load factor> [<debug level>]
 * the first two are required and the third is optional.
 * Input type should be 1 for using java.util.Random,
 * 2 for using System.nanoTime(), or 3 for
 * using the word-list text file.
 * @author Kevin Haines
 */

public class HashTest {
	private static int debug =-1;
	private static HashTable linearTable, doubleTable;
	private static ArrayList<String> wordList;
	private static File inputTextFile;
	private static Scanner scan;

	public static void main(String[] args)  {
		if(args.length<2 || args.length>3){
			System.out.println(printUsage());
		}
		try{
			int arg0 = Integer.parseInt(args[0]);
			double arg1 = Double.parseDouble(args[1]);
			if(arg0!=1 && arg0!=2 && arg0!=3){
				System.out.println(printUsage());
				System.exit(-1);
			}
			if(arg1>=1 || arg1<=0){
				System.out.println(printUsage());
				System.exit(-1);
			}
			if(args.length==3){
				int arg2 = Integer.parseInt(args[2]);
				if(arg2!=0 && arg2!=1 && arg2!=2){
					System.out.println(printUsage());
					System.exit(-1);
				}
			}

		}
		catch(NumberFormatException e){
			System.out.println(printUsage());
			System.exit(-1);
		}
		if(args.length==3)
			debug= Integer.parseInt(args[2]);
		else
			debug=-1;
		String dataType= "";
		if(Integer.parseInt(args[0])==1){
			dataType+="random number generator";
		}
		else if(Integer.parseInt(args[0])==2){
			dataType+="System.nanoTime()";
		}
		else{
			dataType+="words from word-list file";
		}
		if(debug!=-1){
			System.out.println("A good table size is found: "+generateListSize());
			System.out.println("Data source type: "+dataType+"\n");
		}
		if(Integer.parseInt(args[0])==3){
			if(debug>=0)
				System.out.println("Processing word-list text file...");
			generateWordList();
		}

		linearTable = new HashTable(0, generateListSize(), Integer.parseInt(args[0]), Double.parseDouble(args[1]), debug, wordList);
		doubleTable = new HashTable(1, generateListSize(), Integer.parseInt(args[0]), Double.parseDouble(args[1]), debug, wordList);
		debugOutput( debug,args);

	}
	/**
 	 * As per the requirements, this method finds the first
     * twin prime found starting at the value 95,500.  This value
     * is used as the size of the hashtable.
     * @return m, the size of the hashtable
     */

	@SuppressWarnings("resource")
	private static void generateWordList() {
		inputTextFile = new File("../../word-list");
		try {
			scan = new Scanner(inputTextFile);
			wordList =  new ArrayList<String>(generateListSize());
			while (scan.hasNextLine()){
				String nextLine = scan.nextLine();
				Scanner scan2 = new Scanner(nextLine);
				while(scan2.hasNext()){
					wordList.add(scan2.next());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	* This method prints out output to the screen containing
  	* useful information.  The output depends on the debug level.
	* Debug level 2 is handled in the HashTable class.
 	* @param debug indicates the debug level of the program
 	* @param args contains the command line arguemnts for the program
 	*/

	private static void debugOutput(int debug, String[] args){
		if(debug==1){
			for(int i=0; i<linearTable.getN(); i++){
				if(linearTable.getObject(i)!=null)
					System.out.println("table["+i+"]: "+linearTable.getObject(i)+" "+linearTable.getObject(i).getFrequency());
			}
			for(int i=0; i<doubleTable.getN(); i++){
				if(doubleTable.getObject(i)!=null)
					System.out.println("table["+i+"]: "+doubleTable.getObject(i)+" "+doubleTable.getObject(i).getFrequency());
			}
		}
	}

	/**
	 * This function returns the higher prime of the first
	 * twin prime found starting at 95,500 and going up, as per
	 * the assignment requirements.
	 * @return m, the size of the list
	 */
	private static int generateListSize(){

		int num = 95500;
		int lastPrime =0;
		boolean isDone=false;

		while(!isDone){
			if(num%2==0)
				num++;
			else{
				if(isItPrime(num, 2)&&isItPrime(num, 3)&&isItPrime(num, 4)){
					if(lastPrime+2==num){
						isDone=true;
						return num;
					}
					else
						lastPrime=num;
				}
				num++;
			}
		}
		return 0;
	}
	
    /**
     * A method that uses Fermat's theorem to determine
     * if the number is prime.
     * if(a^(p-1))modp==1 then p is most likely prime.
     * 3 values for a are used to increase probability of
     * accuracy(it is very high.)
     *
     * By using the binary value of p we can perform modular
     * division on the calculation as it goes to avoid
     * using a number too large to be held in conventional
     * java number types.
     * @param p the number being determined as prime or not
     * @param a number coprime to p used in fermat's theorem
     * @return 1 if prime and not prime otherwise
     */
    private static boolean isItPrime(int p, int a){
            ArrayList<Integer> binary = intToBinary(p-1);
            long count=a;
            for(int i=1; i<binary.size(); i++){//starts at index 1 to ignore first bit, using it as p-1
                    count = (count*count)%p;
                    if(binary.get(i).equals(new Integer(1)))
                            count = (count*a)%p;
            }
            return count==1;
    }
    /**
      * Method used to return an arraylist representation
      * of num in binary form.
      * @param num the decimal value used to determine the binary return value
      * @return An array list containing Integers, each element being a bit in the binary num
      */
	private static ArrayList<Integer> intToBinary(int num){
		ArrayList<Integer> revBinaryNum = new ArrayList<Integer>();
		ArrayList<Integer> binaryNum= new ArrayList<Integer>();
		while(num!=0){
			revBinaryNum.add(num%2);
			num/=2;
		}
		for(int i=revBinaryNum.size()-1; i>=0;i--){
			binaryNum.add(revBinaryNum.get(i));
		}
		return binaryNum;
	}
	
    /**
      * This method returns a string that will be printed if the
      * user enters invalid command line arguments.
      * @return String used to inform the user the expected input
      */
	private static String printUsage(){
		String retString="";
		retString+= "Invalid command usage. Format must be as follows:\n";
		retString+= "java HashTest <input type> <load factor> [<debug level>]\n";
		retString+= "Input type must be either a 1, 2 or a 3.\n";
		retString+="\t 1: Data generated using java.util.Random.\n";
		retString+="\t 2: Data generated using System.currentTimeMillis().\n";
		retString+="\t 3: Data generated using the file word-list.\n";
		retString+= "Load factor must a precentage represented as a decimal(greater than zero and less than 1.) \n";
		retString+="\tLoad factor ex. 0.5, 0.6, 0.7, 0.8, 0.9, 0.95, 0.98, 0.99.\n";
		retString+="Optional debug argument must be either a 0, 1, or a 2.\n";
		retString+="\t 0: Print summary of experiment.\n";
		retString+="\t 1: Print out hash table at the end.\n";
		retString+="\t 2: Print the number of probes for each new insert.\n";

		return retString;
	}

}
