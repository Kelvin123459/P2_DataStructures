//Kelvin Garcia Mu�iz || Luis Cintr�n Zayas
//802142644 || StudentNumberHere
//CIIC4020 - 030
package waitingPolicies;

import java.util.LinkedList;
import customers.Customer;
import customers.Server;

//Multiple Lines Multiple Servers
public class MLMSBWT {

	int numOfServers, numOfCustomers; 
	long totalTime = -1; // counts the current time of the simulation (begins in time 0)
	private double totalWaitingTime = 0; //total time waited by each customer 
	private LinkedList<Customer> arrivingCustomers; // customers to served
	private LinkedList<Customer> waitingLine; // lists of customers waiting in line
	private Server[]servers; // array of servers

	/**
	 * Constructor
	 * @param customers
	 * @param numberOfServers
	 */
	public MLMSBWT(LinkedList<Customer> customers,int numberOfServers) {
		this.numOfCustomers=customers.size();
		this.arrivingCustomers=copy(customers);
		this.numOfServers=numberOfServers;
		this.servers=new Server[numberOfServers];
		this.waitingLine=new LinkedList<>();
		initializeServers(); // run the server init with the specified number	
	}

	/**
	 * initializes the servers with the specified number of servers
	 */
	public void initializeServers() {
		for(int i=0;i<numOfServers;i++) {
			servers[i]=new Server(); 
		}
	}

	/**
	 * performs the service with a MLMS waiting policy
	 */
	public void Service() {
		//for each server c 
		for(Server c: servers) {
			// if there are servers in line
			if(c.lineLength()!=0) {
				// if the servers has not finished with the customer
				if(c.attending().getServiceTime()!=0) {
					// remove one unit of time from the service time
					c.attending().setServiceTime(c.attending().getServiceTime()-1);
					// increase a unit of time from the departure
					c.attending().setDeparture(c.attending().getDeparture()+1);
				}
				// if the server finished serving the customer
				if(c.attending().getServiceTime()==0) {
					//move on to the next customer (tr is customer already served)
					Customer tr=c.nextCustomer();
					// set the waiting time (= current time - the arrival time of that customer)
					tr.setTimeWaiting((int)totalTime-tr.getArrival());
					// add the waiting time to the total Waiting time
					totalWaitingTime=totalWaitingTime+tr.getTimeWaiting();
					// remove the customer from the arriving customers line
					arrivingCustomers.remove(tr);
				}
			}
		}
	}

	/**
	 * Begins the simulation
	 */
	public void performService() {
		// while the service has not finished (there are customers to be served)
		while(!isEmpty()) {
			// increase the total time by 1 unit
			totalTime++;
			// for each customer c
			for(Customer c: arrivingCustomers) {
				// if the time of arrival is equal to the current time
				if(c.getArrival()==totalTime) {
					// add that customer to the waiting line
					waitingLine.add(c);
				}
			}
			// add the customer to a server with least waiting time
			addToServerAvailable();
			// perform the service
			Service();
		}
		totalTime++; // increases time by one last unit
	}

	/**
	 * Adds the customer to an available server
	 */
	public void addToServerAvailable() {
		int index=0;
		for(int i=1;i<servers.length;i++) {
			// if the line served by the current server is smaller than the line of the first server
			if(servers[i].getWaitingCustTime()<servers[0].getWaitingCustTime())
				// the smallest line is the line of that server
				index=i;
		}
		// if there are customers in line
		if(!waitingLine.isEmpty()){
			long custTime = waitingLine.getFirst().getServiceTime(); //service time of the customer
			servers[index].add(waitingLine.removeFirst()); //add the customer to the server
			servers[index].setWaitingCustTime(custTime); // add the service time of that customer to the service line
		}
	}
	
	/**
	 * Returns a copy of the arriving customers
	 * @param arrivingCustomers
	 * @return
	 */
	private LinkedList<Customer> copy(LinkedList<Customer>arrivingCustomers) {
		LinkedList<Customer> copy= new LinkedList<>();
		// for each customer c
		for(Customer c: arrivingCustomers) {
			//add that customer to the copy
			copy.add(new Customer(c.getArrival(),c.getServiceTime()));
		}
		// return the copy
		return copy;
	}

	/**
	 * returns the average time of the MLMS simulation
	 * @return
	 */
	public double getAverageTime() {
		// average time waited = total time waited / number of customers
		return (totalWaitingTime+1)/numOfCustomers;
	}

	/**
	 * returns the number of Customers to be served
	 * @return
	 */
	public int numberOfCustomer() {
		return numOfCustomers;
	}

	/**
	 * returns the total time taken to serve all the customers
	 * @return
	 */
	public double getTotalTime() {
		return totalTime;
	}

	/**
	 * returns true if all the customers have been served
	 * @return
	 */
	public boolean isEmpty() {
		return arrivingCustomers.isEmpty();
	}
}
