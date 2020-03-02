/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Driver class that initializes all queues and stations, takes user input and runs the simulation then prints the statistics  
 */
 

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Random;

public class PA3
{
	public static void main(String args[])
	{
		int m = Integer.parseInt(args[0]); //mean
		int n = Integer.parseInt(args[1]); //range 
		int q = Integer.parseInt(args[2]); //queueSize
		
		EventHelper e = new EventHelper(m, n); //create eventhelper with mean, range and queuesize 
		
		//set up all the queues 
		ArrayBlockingQueue<Item> q01 = new ArrayBlockingQueue<>(q); //will create a queue that has a set amount of positions (determined by user)
		ArrayBlockingQueue<Item> q12 = new ArrayBlockingQueue<>(q); 
		ArrayBlockingQueue<Item> q23 = new ArrayBlockingQueue<>(q);
		ArrayBlockingQueue<Item> q34 = new ArrayBlockingQueue<>(q);
		ArrayBlockingQueue<Item> q45 = new ArrayBlockingQueue<>(q);
		
		//link stations with queues 
		Station s0 = new StartStation("s0", q01, e); //this station will start the first event 
		Station s1 = new MiddleStation("s1", q01, q12);
		Station s2a = new MiddleStation("s2a", q12, q23);
		Station s2b = new MiddleStation("s2b", q12, q23);
		Station s3 = new MiddleStation("s3", q23, q34);
		Station s4a = new MiddleStation("s4a", q34, q45);
		Station s4b = new MiddleStation("s4b", q34, q45);
		Station s5 = new FinalStation("s5", q45);
		
		//link stations with other stations 
		s0.addNext(s1);
        s1.addPrev(s0);
        s1.addNext(s2a);
        s1.addNext(s2b);
        s2a.addPrev(s1);
        s2a.addNext(s3);
        s2b.addPrev(s1);
        s2b.addNext(s3);
        s3.addPrev(s2a);
        s3.addPrev(s2b);
        s3.addNext(s4a);
        s3.addNext(s4b);
        s4a.addPrev(s3);
        s4a.addNext(s5);
        s4b.addPrev(s3);
        s4b.addNext(s5);
        s5.addPrev(s4a);
        s5.addPrev(s4b);
	
		//run sim and print results 
		e.runSimulation();
		e.printStatistics(s0, s1, s2a, s2b, s3, s4a, s4b, s5);
	}
}