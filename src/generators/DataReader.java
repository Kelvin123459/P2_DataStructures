package generators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

import customers.Customer;
import waitingPolicies.MLMS;
import waitingPolicies.SLMS;
import implementations.ArrayQueue;
import implementations.LinkedQueue;

public class DataReader{

	public static void main(String[] args) throws IOException {
		
		LinkedQueue<Customer> arrivalQueue = new LinkedQueue<Customer>();
		LinkedQueue<Customer> serviceStartsQueue = new LinkedQueue<Customer>();
		ArrayQueue<Customer> serviceCompletedQueue = new ArrayQueue<Customer>();
		int n;
		double averageTime, value;
		
		
		String parentDirectory = "inputFiles";
		Scanner parameters = new Scanner(new File(parentDirectory, "parameters.txt")); 
		// the values of n and m shall be read from file: "inputFiles/parameters.txt". 
		n = parameters.nextInt(); 
		String fileName = "data_0.txt"; 
		
		File inputfile = new File(parentDirectory, fileName);
		BufferedReader dataReader = null;
        String dataline;
        int arrTime = 0;
        int serTime =0;

            dataReader = new BufferedReader(new FileReader(inputfile));
            while ((dataline = dataReader.readLine()) != null) {

               String[] data =dataline.split(",");             
              
               arrTime = Integer.parseInt(data[0]);
               serTime = Integer.parseInt(data[1].substring(data[1].length()-1));
               Customer element = new Customer(0, arrTime, serTime);
              
              arrivalQueue.enqueue(element);  
              
            }           
            if (dataReader != null) {
                    dataReader.close();

        }
    
        SLMS serv1 = new SLMS(arrivalQueue, serviceStartsQueue, serviceCompletedQueue);
        int numofServers = 1;
        

        	serv1.performService(numofServers);
//  	value = (double)(arrTime - serv1.getsServedTime())/serviceCompletedQueue.size();
       	value = time(serviceCompletedQueue);
//   		averageTime = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
        	
       
        	System.out.print("\nSLMS "+numofServers+": "+serv1.getServedTime()+" ");
        	System.out.printf("%.2f", value);
        	numofServers+=2;
             
	}
	
	
	public static ArrayQueue<Customer> copyOf (ArrayQueue<Customer> list) {
		ArrayQueue<Customer> copy = new ArrayQueue<>();
		
		int i = 0;
		while(!(i==list.size())) {
			Customer c = list.dequeue();
			Customer copyC = new Customer();
			
			copyC.setArrival(c.getArrival());
			copyC.setDeparture(c.getDeparture());
			copyC.setiD(c.getID());
			copyC.setServiceTime(c.getServiceTime());
			
			list.enqueue(c);
			copy.enqueue(copyC);
			i++;
		}
		
		return copy;
	}

	public static float time(ArrayQueue<Customer> serviceCompletedQueue ) {
		   //Calculates time in system
  		float totalTime = 0;
  		float arrVal = 0;
  		float serVal = 0;
  		int i =0;
  		ArrayQueue<Customer> copyServiceCompletedQueue = copyOf(serviceCompletedQueue);
  		while(!(copyServiceCompletedQueue.isEmpty())) {
  			arrVal = copyServiceCompletedQueue.first().getArrival();
  			serVal = copyServiceCompletedQueue.first().getDeparture()-arrVal;
  			totalTime= (arrVal - serVal ) + totalTime;
  			
  			i++;
  			copyServiceCompletedQueue.dequeue();
  		}
  		totalTime= totalTime/i;
  	
  		return totalTime;
	}
	
	
}