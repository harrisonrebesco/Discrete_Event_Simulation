/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Provides helper methods to other classes to perform simulation, also compiles and prints simulation statistics 
 */
 
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Random;

public class EventHelper
{
	private double simulationTime = 0.0; //the time, will be used to drive the simulation, updated every time an event is finished 
	private int mean; 
	private int range;
	private Random r = new Random(8);
	private PriorityQueue<Event> eventQueue = new PriorityQueue<>(5); //queue of events to be executed 
	private ArrayList<Item> finishedItems; //array that holds all the finished items - will be used to calculate simulation statistics 

	//default constructor 
	EventHelper(){}
	
	//constructor that takes mean, range and queueSize parameters 
	public EventHelper(int m, int n)
	{
		mean = m; 
		range = n;
		finishedItems = new ArrayList<Item>(); //initialize finishedItems 
	}

	//method to run the simulation 
	//PRE: eventHelper should be initialized with mean, range and queueSize 
	//POST: runs the simulation until it exceeds 10000000 units of time 
	public void runSimulation()
	{
		while (simulationTime < 10000000) 
		{ 
			Event e = eventQueue.poll(); //pulls the first event from eventQueue 
			simulationTime = e.getEndTime(); //increments simulation time 
			Station s = e.getEventStation(); //gets event station 
            s.productionLine(this); //execute production 
        }
	}
	
	//method to print statistics of simulation
	//PRE: simulation must be complete
	//POST: prints production stations, storage queue statistics, and production paths 
	public void printStatistics(Station s0, Station s1, Station s2a, Station s2b,Station s3, Station s4a, Station s4b, Station s5)
	{
		//print station statistics 
		System.out.println("\nPRODUCTION STATIONS:"); 
		System.out.println("----------------------------------------");
		System.out.printf("%4s %7s %11s %12s", "Station:", "Work[%]", "Starve[t]", "Block[t]" + "\n");
		System.out.println("----------------------------------------");
		System.out.println(s0.stationStats()); 
		System.out.println(s1.stationStats());
		System.out.println(s2a.stationStats());
		System.out.println(s2b.stationStats());
		System.out.println(s3.stationStats());
		System.out.println(s4a.stationStats());
		System.out.println(s4b.stationStats());
		System.out.println(s5.stationStats());
		System.out.println("---------------------------------------- \n");
		
		//print storage queue statistics 
		System.out.println("STORAGE QUEUE STATISTICS:");
		System.out.println("--------------------------");
		System.out.printf("%6s %8s %8s", "Queue:", "AvgTime[t]", "AvgItems" + "\n");
		System.out.println("--------------------------");
		storageQueueStats(); 
		System.out.println("-------------------------- \n");
		
		//print production path statistics
		System.out.println("PRODUCTION PATHS: ");
		System.out.println("--------------------");
		System.out.printf("%10s %10s","PathTaken:", "Qty:" + "\n");
		System.out.println("--------------------");
		productionPathStats();  
		System.out.println("-------------------- \n");
	}
	
	//method that prints the storage queue statistics
	//PRE: simulation must be complete
	//POST: all items in finished item array are printed 
	private void storageQueueStats()
	{
		int itemCount = 0; //counts total items 
		int queueTime [] = new int [5]; //used to get the queueIO time stamps 
		
		//traverse all items in finishedItems and retrieve stats 
		for (Item i : finishedItems)
		{
			if (i != null)
			{
				double queueEntry [] = i.getQueueEntry(); 
				double queueExit[] = i.getQueueExit();
				
				queueTime[0] += queueExit[1] - queueEntry[0]; //computes total time spent in queue 1
				queueTime[1] += queueExit[2] - queueEntry[1]; //computes total time spent in queue 2
				queueTime[2] += queueExit[3] - queueEntry[2]; //computes total time spent in queue 3
				queueTime[3] += queueExit[4] - queueEntry[3]; //computes total time spent in queue 4
				queueTime[4] += queueExit[5] - queueEntry[4]; //computes total time spent in queue 5

				itemCount++; //increment total items 
			}
		}
		
		//find the total amount of time items were in a queue by adding up each queue time 
		double totalTime = 0; 
		for (int i = 0; i < 5; i++)
			totalTime += queueTime[i]; 
		
		//find the average modifier (average time each item has spent in a queue) by dividing total items by the total time spent in queues 
		double aveMod = itemCount/totalTime;
		
		//for each queue, print statisitcs 
		for (int i = 0; i < 5; i++)
		{
			double aveTime = queueTime[i];  
			double aveItems = aveMod * queueTime[i]; //adjust queue time by average modifier 
			
			String timeOutput = String.format("%4.2f", aveTime/itemCount); //format average time 
			String itemOutput = String.format("%4.2f", aveItems/1000); //format average items 
			String output = "";
			output += String.format("%6s", "Q" + (i + 1));
			output += String.format("%11s", timeOutput);
			output += String.format("%9s", itemOutput);
			
			System.out.println(output); //print statistics 
		}
	}
	
	//method that prints the production path statistics
	//PRE: simulation must be complete
	//POST: all items in finished item array are printed 
	private void productionPathStats()
	{		
		int a = 0, b = 0, c = 0, d = 0; //each of these represent a queuepath 
		String pathA, pathB, pathC, pathD;
		
		//for each item, compare to a string and increment total if they match 
		for (Item i : finishedItems)
		{
			if (i != null)
			{
				if (i.getEventPath().equals("s0 s1 s2a s3 s4a s5 "))
					a++;
				else if (i.getEventPath().equals("s0 s1 s2a s3 s4b s5 "))
					b++;
				else if (i.getEventPath().equals("s0 s1 s2b s3 s4a s5 "))
					c++;
				else 
					d++;
			}
		}
		
		//compile info and print 
		pathA = "s2a -> s4a";
		pathB = "s2a -> s4b";
		pathC = "s2b -> s4a";
		pathD = "s2b -> s4b";
		System.out.println(pathA + "\t" + a);
		System.out.println(pathB + "\t" + b);
		System.out.println(pathC + "\t" + c);
		System.out.println(pathD + "\t" + d);
	}
	
	//method that adds an item to the finished item array 
	//PRE: must be an item 
	//POST: item added to array 
	public void addFinishedItem(Item i)
	{
		finishedItems.add(i);
	}
	
	//method that gets the next random number 
	//PRE: n/a 
	//POST: returns a random number 
	public double getRandom()
	{
		return r.nextDouble();
	}
	
	//method that creates an event based on a station
	//PRE: must be a station that is passed 
	//POST: event will be added to the queue waiting to be executed 
	public void addEvent(Station s)
	{
		eventQueue.add(new Event(s, this));
	}
	
	//method that sets the mean 
	//PRE: must be an int 
	//POST: mean is set 
	public void setMean(int m)
	{
		mean = m;
	}
	
	//method to return the mean 
	//PRE: n/a 
	//POST: mean is returned 
	public int getMean()
	{
		return mean;
	}
	
	
	//method that sets the range 
	//PRE: must be an int 
	//POST: range is set 
	public void setRange(int n)
	{
		range = n;
	}
	
	//method to return the mean 
	//PRE: n/a 
	//POST: mean is returned 
	public int getRange()
	{
		return range;
	}
	
	
	//method to set the simulation time 
	//PRE: must be a double 
	//POST: simulation time is set 
	public void setSimulationTime(double t)
	{
		simulationTime += t;
	}
	
	//method to return the simulation time 
	//PRE: n/a 
	//POST: simulation time is returned 
	public double getSimulationTime()
	{
		return simulationTime;
	}
}