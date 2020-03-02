/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Models the middle Stations, (s1, s2a/b, s3, s4a/b) - can be starving or blocking, can get and send items.
 */

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class MiddleStation extends Station
{
    private ArrayBlockingQueue<Item> nextQueue; //item storage between stations
    private ArrayBlockingQueue<Item> prevQueue; //item storage between stations
	private ArrayList<Station> nextStation; //next station in sequence 
    private ArrayList<Station> prevStation; //previous station in sequence 
	
	//default constructor 
	MiddleStation(){}
	
	//constructor with name, next, previous parameters 
	MiddleStation(String n, ArrayBlockingQueue<Item> prev, ArrayBlockingQueue<Item> next)
	{
		setName(n); //sets the name of station 
		nextStation = new ArrayList<>(); //initialize next station
		prevStation = new ArrayList<>(); //initialize prev station 
		prevQueue = prev; //set next queue 
		nextQueue = next; //set prev queue 
		setStarved(true); //station begins starving 
		setBlocked(false); //station begins unblocked 
	}

	//override abstract addNext to allow a next Station (Middle Station has a next and previous Station)
	@Override
	public void addNext(Station n)
	{
		nextStation.add(n);
	}
	
	//override abstract addNext to allow a next Station (Middle Station has a next and previous Station)
	@Override
	public void addPrev(Station p)
	{
		prevStation.add(p);
	}
	
	//override startProduction to accurately model the MiddleStation requirements 
	@Override
	public void productionLine(EventHelper e)
	{
		double simulationTime = e.getSimulationTime(); //gets the current time from event helper 

		//check to see if station is blocking
		if (isBlocking())
		{
			setBlocked(false); //unblock station 
			updateBlockTime(simulationTime - getTime()); //calculate the difference in time since blocked, increment total block time 
		}
		
		//check to see if station is starving
		if (isStarving())
		{
			setStarved(false); //unstarve station 
			updateStarveTime(simulationTime - getTime()); //calculate the difference in time since starved, increment total starve time 
		}

		//check that station has an item
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
				//if next queue is not full, finish production of item and add to queue 
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
		{
			//check that previous queue is empty 
			if (prevQueue.isEmpty())
			{	
				//if previous queue is empty, starve station and set time (will be used later to calulate time starved)
				setStarved(true);
				setTime(e.getSimulationTime());
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