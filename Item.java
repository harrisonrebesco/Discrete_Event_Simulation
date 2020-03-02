/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Item class that stores event path, and tracks when item enters and exits queue 
 */
 
public class Item
{
	private String eventPath =""; //stores the path that item has taken
	private double [] queueEntry; //stores the entry times of items in each queue - to calculate total time spent in each queue 
	private double [] queueExit; //stores the entry times of items in each queue - to calculate total time spent in each queue
	private int i, j; //used to help itemTimes, will increment to track unique item times 
	
	//default constructor 
	Item()
	{
		queueEntry = new double[6]; //initialize queueEntry - this will track when item enters a queue
		queueExit = new double[6]; //initialize queueExit - this will track when item exits a queue 
		i = 0; //will count enter index 
		j = 0; //will count exit index 
	}
	
	//method that sets the path an item takes 
	//PRE: must take a string as parameter 
	//POST: event path is updated to reflect the path an item takes 
	public void setEventPath(String p)
	{
		eventPath += p;
	}
	
	//method that returns the event path 
	//PRE: n/a
	//POST: returns path 
	public String getEventPath()
	{
		return eventPath;
	}

	//method that updates queueEntry - this is used in finishProduction() to track the times when is placed in queue. 
	//PRE: must take double as parameter 
	//POST: tracks the entry times of an item in each queue
	public void setQueueEntry(double t)
	{
		queueEntry[i] = t;
		i++;
	}
	
	//method that returns queueEntry. 
	//PRE: n/a 
	//POST: returns queueEntry
	public double [] getQueueEntry()
	{
		return queueEntry;
	}
	
	//method that updates queueExit - this is used in startProduction() to track the times when item is removed from queue. 
	//PRE: must take double as parameter 
	//POST: tracks the exit times of an item in each queue
	public void setQueueExit(double t)
	{
		queueExit[j] = t;
		j++; 
	}
	
	//method that returns queueExit. 
	//PRE: n/a 
	//POST: returns queueExit
	public double [] getQueueExit()
	{
		return queueExit;
	}
}