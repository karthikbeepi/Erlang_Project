package CompleteReShuffle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

class Pair{
	String name;
	int value;
	public Pair(String n, int val) {
		name = n;
		value = val;
	}
}

class ReadFromFile {

	BufferedReader br;
	String type;
	
	public ReadFromFile(String filePath, String type) {
	
		try {
			
			br = new BufferedReader(new FileReader(filePath));
			
			this.type = type;
			
		} catch (FileNotFoundException e) {
		
			e.printStackTrace();
		}
		
	}

	public Pair next() {
		
		try {
			String str = br.readLine();
			if(str==null)
				return null;
			int posComma = str.indexOf(",");
			return new Pair(str.substring(1, posComma), Integer.parseInt(str.substring(posComma+1, str.length()-2)));
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		
		return null;
	}	
}

public class money {
	HashMap<String , Integer> banks = new HashMap<>();
	HashMap<String, Integer> customers = new HashMap<>();
	ArrayList<String> customersDone = new ArrayList<>();
	
	LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
	
	Thread[] bankThreads;
	Thread[] customerThreads;
	
	public money() {
		
		File testFile = new File("");
		String currentPath = testFile.getAbsolutePath();
		System.out.println("current path is: " + currentPath);
		ReadFromFile readCustomer;
		ReadFromFile readBank;
//		if(!currentPath.contains("src/CompleteReShuffle/"))
//		{
//			readCustomer = new ReadFromFile("./src/CompleteReShuffle/customers.txt", "customers");
//			readBank = new ReadFromFile("./src/CompleteReShuffle/banks.txt", "banks");
//		}
//		else
//		{
//			readCustomer = new ReadFromFile("./src/CompleteReShuffle/customers.txt", "customers");
//			readBank = new ReadFromFile("./src/CompleteReShuffle/banks.txt", "banks");	
//		}
		
		readCustomer = new ReadFromFile("/home/kbeepi/Comparative-Project/src/CompleteReShuffle/customers.txt", "customers");
		readBank = new ReadFromFile("/home/kbeepi/Comparative-Project/src/CompleteReShuffle/banks.txt", "banks.txt");
		
		Pair temp;
		System.out.println("*** Customers and loan objectives ***");
		while(true)
		{
			temp =  readCustomer.next();
			if(temp==null)
				break;
			customers.put(temp.name, temp.value);
			System.out.println(temp.name+": "+(int)temp.value);
			
		}
		
		System.out.println("\n*** Banks and financial resources ***");
		while(true)
		{
			temp =  readBank.next();
			if(temp==null)
				break;
			banks.put(temp.name, temp.value);
			System.out.println(temp.name+": "+(int)temp.value);
		}
		
		System.out.println();
	}
	
	public static void main(String[] args) {
		
		money obj = new money();
		obj.runBanks();
		obj.runCustomers();
		
	}

private void runCustomers() {
		
		customerThreads = new Thread[customers.size()];
		int i=0;
		for(String s: customers.keySet())
		{
			customerThreads[i] = new Thread(new Customer(s, customers.get(s), queue, banks.keySet(), customersDone));
			customerThreads[i].start();
			i++;
		}
	}

	private void runBanks() {
		
		bankThreads = new Thread[banks.size()];
		int i=0;
		for(String s: banks.keySet())
		{
			bankThreads[i] = new Thread(new Bank(s, banks.get(s), queue, customersDone, customers.keySet()));
			bankThreads[i].start();
			i++;
		}
		
	}
	
}
