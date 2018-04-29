//Kelvin Garcia Mu�iz || Luis Cintr�n Zayas
//802142644 || StudentNumberHere
//CIIC4020 - 030
package testerClasses;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;
import customers.Customer;
import waitingPolicies.SLMS;

/**
 * Main class tester. Runs all simulations
 * @author Kelvin Garc�a Mu�iz & Luis Cintr�n Zayas
 *
 */
public class Main {
	public static void main(String[] args) {
		String directory = "inputFiles"; 	//directory containing the input files
		String directory2 = "outputFiles";	//directory containing the output files
		String dataFile = "dataFiles.txt";	//file containing the names of the files to be tested
		Scanner scanner; // reads the dataFiles from dataFiles.txt
		Scanner scanner2; // reads each data file listed in dataFiles.txt
		SLMS[] policy1 = new SLMS[3]; //SLMS policy
		ArrayList<Customer> list = new ArrayList<>(); 
		ArrayList<Customer> list2 = new ArrayList<>(); // used as a copy of list
		NumberFormat formatter = new DecimalFormat("#0.00"); //formats the output to 2 decimal numbers
		int counter=0, numOfServers = 1; //counter used to keep track of the file name && default number of servers
		final int numOfTests = 3; // the number of tests to be performed 
		long arrivalTime, serviceTime; // the arrival and service time of each customer
		try { //begins try/catch 1
			scanner = new Scanner(new File(directory, dataFile)); //initialize scanner
			while(scanner.hasNextLine()){ // while there are customers in the file
				// initialize writer for each output file
				PrintWriter out = new PrintWriter(new File(directory2, "data_" + counter + "_OUT.txt")); 
				String file = scanner.nextLine(); //set the current file being tested
				System.out.println(file); // Prints on console the file being tested (for testing purposes)
				try { // begins try/catch 2
					scanner2 = new Scanner(new File(directory, file)); //initialize scanner 2
					initializePolicies(policy1); //intialize SLMS policy
					while(scanner2.hasNextInt()){ //while there are integers in the file
						arrivalTime = scanner2.nextInt(); // the arrival time equals the first number in each line of the file
						serviceTime =scanner2.nextInt();  // the service time equals the second number in each line of the file
						// initialize a new customer with corresponding values
						Customer customer = new Customer(arrivalTime, serviceTime); 
						// add the customer to the list
						list.add(customer); 
					}
					scanner2.close(); //close scanner2
					out.println("-----------------------------------"); 
					for(int i =0; i<numOfTests ; i++) { //runs three tests
						list2 = copyList(list); //makes a copy of the list
						policy1[i].performService(numOfServers, list2); // performs service with the respective num of servers
						out.println("SLMS " + numOfServers+ ": " + policy1[i].getsTotalTime()+ "\t" + 
								formatter.format(policy1[i].getAverageWaiting())); // prints the output on its respective file
						numOfServers+=2; //increase the number of servers by 2
					}
					out.println("-----------------------------------");
				} catch (FileNotFoundException e) { // if the input file was not found
					out.println("Input file not found.");
				}//end of try/catch 2
				out.close(); // close the output printer
				counter++; // increase the file counter
			}
			scanner.close(); // close scanner
		} catch (FileNotFoundException e1) {
			System.out.println("The directory path specified was not found"); //if the output directory was not found
		}
	}
	
	/**
	 * returns a copy of the specified list
	 * @param list
	 * @return 
	 */
	private static ArrayList<Customer> copyList(ArrayList<Customer> list){
		ArrayList<Customer> list2 = new ArrayList<>();
		// for each customer in list
		for(Customer E: list){
			// add the customer to the copy
			list2.add(E);
		}
		//return the copy
		return list2;
	}

	/**
	 * initializes the policy 
	 * @param policy
	 */
	private static void initializePolicies(SLMS[]policy) {
		for(int i = 0; i< 3;i++) {
			policy[i]= new SLMS();
		}
	}

}