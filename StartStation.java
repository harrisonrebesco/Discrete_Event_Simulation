/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Models the first Station (s0) - can never be starving but block, and can only send items. 
 */

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class StartStation extends Station
{
	private ArrayBlockingQueue<Item> nextQueue; //Item storage between stations 
	private ArrayList<Station> nextStation; //next station in sequence 
	
	//default constructor 
	StartStation(){}
	
	//constructor with name, next, and eventhelper parameters 
	StartStation(String n, ArrayBlockingQueue<Item> next, EventHelper e)
	{
		setName(n); //sets name of station 
		startProduction(new Item(), e); //because this is the first station, create a new item. (this will trigger a new event)
		nextStation = new ArrayList<>(); //initialize next station 
		nextQueue = next; //set next queue 
		setBlocked(false); //station begins unblocked  
	}
	
	//override abstract addNext to allow a next Station (Start Station has a next but not a previous)
	@Override
	public void addNext(Station n)
	{
		nextStation.add(n);
	}
	
	//override abstract addPrev to disallow a previous station (Start Station has a next but not a previous)
	@Override
	public void addPrev(Station p)
	{
		throw new IllegalStateException("Error. StartStation cant have previous");
	}
	
	//override productionLine to accurately model the StartStation requirements 
	@Override
	public void productionLine(EventHelper e)
	{
		double simulationTime = e.getSimulationTime(); //gets the current time from event helper 
		
		//check if station is blocked
		if (isBlocking())
		{
			setBlocked(false); //unblock station 
			updateBlockTime(simulationTime - getTime()); //calculate the difference in time since blocked, increment total block time 
		}

		//check that station has a item 
		if (getItem() != null)
		{
			//check if next queue is full 
			if (nextQueue.remainingCapacity() == 0) 
			{
				//if next queue is at capacity block station, and set time (will be used to calculate time blocked)
				setBlocked(true); 
				setTime(simulationTime); 
				return; //force method to return, as we do not want to continue if station is blocked 
			}
			else 
			{
				//if next queue has room, finish production of Item and add to queue 
				Item i = finishProduction(e); //this will track time item is placed in queue, and set item to null, allowing production of a new item
				nextQueue.add(i); 
				
				//loop through next stations (will be max 2) and check if they're starving, if starving "force" production of new item
				for (int j = 0; j < nextStation.size(); j++)
				{
					Station s = nextStation.get(j); //gets next station 
					if (s.isStarving()) //check if starving 
						s.productionLine(e); //start production 
				}
			}
		}
		
		//check that station doesnt have an item 
		if (getItem() == null)
			startProduction(new Item(), e); //creates new item in station (this will trigger a new event)
	}
}