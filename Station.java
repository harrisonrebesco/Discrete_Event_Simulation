/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Abstract class that provides foundation for StartStation, MiddleStation and FinalStation. Also calculates Station statistics 
 */

public abstract class Station
{
	private String stationName; 
	private Item item; 
	private double time = 0.0;	//used to track how long blocked or starved
	private double bTime = 0.0; //time blocked 
	private double sTime = 0.0; //time starved
	private double pTime = 0.0; //time in production
	private boolean blocked;
	private boolean starved;
	
	//abstract method to process a item 
	//PRE: must have eventhelper to provide helper funcitons 
	//POST: will assess next/previous queues and stations to process items 
	public abstract void productionLine(EventHelper e);
	
	//abstract method to add next station 
	//PRE: n/a
	//POST: next station added 
	public abstract void addNext(Station n);

	//abstract method to add prev station 
	//PRE: n/a
	//POST: prev station added 
	public abstract void addPrev(Station p);
	
	//method to set station item 
	//PRE: n/a 
	//POST: item is set for station
	protected void setItem(Item i)
	{
		item = i;
	}
	
	//method that returns item within station
	//PRE: n/a 
	//POST: item is returned
	protected Item getItem()
	{
		return item;
	}
	
	//method to set station item 
	//method to set station item 
	//PRE: n/a 
	//POST: item event path and queue interactions are updated and an event is created
	protected void startProduction(Item i, EventHelper e)
	{
		i.setQueueExit(e.getSimulationTime()); //adds time stamp of item (used to track time taken out of queue)
		i.setEventPath(stationName + " "); //updates event path of item (used to tract path taken within simulation)
		setItem(i);
		e.addEvent(this); //adds new event for item to be processed 
	}
	
	//method used when moving item to next station, will take a time stamp of queue entry and set item to null
	//PRE: n/a 
	//POST: item queue interactions are updated and item is removed from station as it is placed into queue 
	protected Item finishProduction(EventHelper e)
	{
        Item tempItem = item; //use temp item to access item 
        item = null; //set item to null as it is no longer required 
        if (tempItem != null) 
            tempItem.setQueueEntry(e.getSimulationTime()); //adds time stamp of item (used to track time put into queue) 
        return tempItem; //return temp item storing item 
	}
	
	//method to put station in a starved state 
	//PRE: n/a
	//POST: station is set to "starved", meaning the previous queue is empty 
	protected void setStarved(boolean b)
	{
		starved = b;
	}
	
	//method to query the state of the station 
	//PRE: n/a 
	//POST: returns true/false depending on starved state
	protected boolean isStarving()
	{
		return starved;
	}
	
	//method to put station in a blocked state 
	//PRE: n/a
	//POST: station is set to "blocked", meaning the next queue is full
	protected void setBlocked(boolean b)
	{
		blocked = b;
	}
		
	//method to query the state of the station 
	//PRE: n/a 
	//POST: returns true/false depending on blocked state
	protected boolean isBlocking()
	{
		return blocked;
	}
	
	//method to set the name of the station
	//PRE: must be a string
	//POST: name is set for station 
	protected void setName(String n) 
	{
		stationName = n;
    }

	//method to query the name of the station 
	//PRE: n/a 
	//POST: returns the station name
    protected String getName() 
	{
        return stationName;
    }

	//method to set time of station, mainly used as a helper function to determine the time passed while blocking or starving 
	//PRE: must be double 
	//POST: time is stored 
	protected void setTime(double t) 
	{
		time = t;
    }

	//method to query the time of the station 
	//PRE: n/a
	//POST: returns station time 
    protected double getTime() 
	{
        return time;
    }
	
	//method to increment the block time, used to keep track of total time station is blocking 
	//PRE: must be double
	//POST: total time blocking is incremented 
	protected void updateBlockTime(double t) 
	{
        bTime += t;
    }

	//method to return the time blocked in a formatted string
	//PRE: blocktime cant be null
	//POST: time blocked is returned in 4.2f format 
	private String getBlockTime()
	{
		String blockTime = String.format("%4.2f", bTime);
		return blockTime;
	}

	//method to increment the starve time, used to keep track of total time station is starving
	//PRE: must be double
	//POST: total time starving is incremented 
    protected void updateStarveTime(double t) 
	{
        sTime += t;
    }

	//method to return the time starving in a formatted string
	//PRE: starvetime cant be null
	//POST: time starved is returned in 4.2f format 
	private String getStarveTime()
	{
		String starveTime = String.format("%4.2f", sTime);
		return starveTime;
	}

	//method to increment the production time, used to keep track of total time station is in production
	//PRE: must be double
	//POST: total production time is incremented 
    protected void updateProductionTime(double t) 
	{
        pTime += t;
    }
	
	//method to return the production time as a percentage in a formatted string
	//PRE: cant be null
	//POST: production time is returned as a percentage in 4.2f format 
	private String getProductionPercent()
	{
		String productionTime = String.format("%4.2f", (pTime / (pTime + bTime + sTime) ) * 100); //finds the percentage of time station has spent in production 
		return productionTime;
	}
	
	//method to format and display station statistics 
	//PRE: production, starve, and block times cant be null
	//POST: the time spent starved, blocked, and % of time spent in production is formatted and returned as a string
	public String stationStats() 
	{
        String output = "";
        
		output += String.format("%6s", stationName); //gets name 
        output += String.format("%10s", getProductionPercent()); //gets production percent 
        output += String.format("%12s", getStarveTime()); //gets starve time 
        output += String.format("%12s", getBlockTime()); //gets block time 
		
        return output;
    }
}