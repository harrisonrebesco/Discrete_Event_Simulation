/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Models the last Station (s5) - can starve but never be blocking, and can only get items.
 */

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class FinalStation extends Station
{
	private ArrayBlockingQueue<Item> prevQueue; //item storage between stations 
	private ArrayList<Station> prevStation; //previous station in sequence
	
	//default constructor 
	FinalStation(){}
	
	//constructor with name and previous parameters 
	FinalStation(String n, ArrayBlockingQueue<Item> prev)
	{
		setName(n); //sets the name of station 
		prevStation = new ArrayList<>(); //initialize previous station 
		prevQueue = prev; //set prev queue 
		setStarved(true); //station begins starved 
		setBlocked(false); //stations begins unblocked 
	}
	
	//override abstract addNext to disallow a next Station (Final Station has a no next station)
	@Override
	public void addNext(Station n)
	{
		throw new IllegalStateException("This is not allowed. FinalStation cannot have a next Station.");
	}
	
	//override abstract addNext to allow a next Station (Final Station has a previous station)
	@Override
	public void addPrev(Station p)
	{
		prevStation.add(p);
	}
	
	//override startProduction to accurately model the FinalStation requirements 
	@Override
	public void productionLine(EventHelper e)
	{
		double simulationTime = e.getSimulationTime(); //gets the current time from event helper 
		
		//check to see if station is starving 
		if (isStarving())
		{
			setStarved(false); //unstarve station 
			updateStarveTime(simulationTime - getTime()); //calculate the difference in time since starved, increment total starve time 
		}
		
		//check that station has an item 
		if (getItem() != null)
		{
			//finish production of item and add to finishedItem array 
			Item i = finishProduction(e); //this will set item to null, allowing production of a new item
			e.addFinishedItem(i); //add item to finished array (used to compile item statistics)
		}
		
		//check that station doesnt have an item 
		if (getItem() == null)
		{
			//check if previous queue is empty 
			if (prevQueue.isEmpty())
			{
				//if previous queue is empty, starve station and set time (will be used later to calulate time starved)
				setStarved(true);
				setTime(simulationTime);
			}
			else 
			{
				//if previous queue is not empty, start production of a new item 
				Item i = prevQueue.poll(); //pulls item from previous queue 
				startProduction(i, e); //starts production of item (will create a new event)
				
				//loop through previous stations (will be max 2) and check if they're blocking, if blocking "force" production of new item
				for (int j = 0; j < prevStation.size(); j++)
				{
					Station s = prevStation.get(j); //gets next station 
					if (s.isBlocking()) //check if blocking 
						s.productionLine(e); //start production 
				}
			}
		}
	}
}