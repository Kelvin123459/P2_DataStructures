//Kelvin Garcia Mu�iz || Luis Cintr�n Zayas
//802142644 || 841141275
//CIIC4020 - 030
package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Scanner;

import simulationObjects.Customer;
import waitingPolicies.MLMS;
import waitingPolicies.MLMSBLL;
import waitingPolicies.MLMSBWT;
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
		LinkedList<Customer> list = new LinkedList<>(); 
		LinkedList<Customer> list2 = new LinkedList<>(); // used as a copy of list
		NumberFormat formatter = new DecimalFormat("#0.00"); //formats the output to 2 decimal numbers
		int counter=0, numOfServers; //counter used to keep track of the file name && number of servers
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
					if(scanner2.hasNextInt()){ // if the files contains numbers only
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
						numOfServers = 1;
						for(int i =0; i<numOfTests ; i++) { //runs three tests
							list2 = copyList(list); //makes a copy of the list
							policy1[i].performService(numOfServers, list2); // performs service with the respective num of servers
							out.println("SLMS " + numOfServers+ ": " + policy1[i].getsTotalTime()+ "\t\t\t" + 
									formatter.format(policy1[i].getAverageWaiting())+"\t"+ 0.0); // prints the output on its respective file
							//SLMS will always have m = to 0 since there are no other lines and no transfers can occur
							numOfServers+=2; //increase the number of servers by 2
						}
						out.println("-----------------------------------"); 
						numOfServers =1; // resets the number of servers
						for(int i =0; i<numOfTests ; i++) { //runs three tests
							list2 = copyList(list); //makes a copy of the list
							MLMS policy2= new MLMS(list2,numOfServers); // creates the policy with its respective parameters
							policy2.performService(); // performs service
							out.println("MLMS " + numOfServers+ ": " + policy2.getTotalTime()+ "\t\t" + 
									formatter.format(policy2.getAvgWaitingTime())+"\t"+policy2.calculateM()); // prints the output on its respective file
							numOfServers+=2; //increase the number of servers by 2
						}
						out.println("-----------------------------------"); 
						numOfServers =1; // resets the number of servers
						for(int i =0; i<numOfTests ; i++) { //runs three tests
							list2 = copyList(list); //makes a copy of the list
							MLMSBLL policy3= new MLMSBLL(list2,numOfServers); // creates the policy with its respective parameters
							policy3.performService(); // performs service
							out.println("MLMSBLL " + numOfServers+ ": " + policy3.getTotalTime()+ "\t\t" + 
									formatter.format(policy3.getAvgWaitingTime())+"\t"+policy3.calculateM()); // prints the output on its respective file
							numOfServers+=2; //increase the number of servers by 2
						}
						out.println("-----------------------------------"); 
						numOfServers =1; // resets the number of servers
						for(int i =0; i<numOfTests ; i++) { //runs three tests
							list2 = copyList(list); //makes a copy of the list
							MLMSBWT policy4= new MLMSBWT(list2,numOfServers); // creates the policy with its respective parameters
							policy4.performService(); // performs service
							out.println("MLMSBWT " + numOfServers+ ": " + policy4.getTotalTime()+ "\t\t" + 
									formatter.format(policy4.getAvgWaitingTime())+"\t"+policy4.calculateM()); // prints the output on its respective file
							numOfServers+=2; //increase the number of servers by 2
						}
						out.println("-----------------------------------");
					} // if the file is invalid
					else{
						out.println("Input file does not meet the expected format or it is empty.");
					}
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
	private static LinkedList<Customer> copyList(LinkedList<Customer> list){
		LinkedList<Customer> list2 = new LinkedList<>();
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
