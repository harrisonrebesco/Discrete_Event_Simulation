/* SENG2200 - ASSIGNMENT 3
 * Author: Harrison Rebesco 
 * Student Number: c3237487  
 * Date: 4/6/19
 * Description: Creates an event, which will be added to a priority queue of events which will ultimately drive the simulation.
 * 				event priority is determined by the end time of the event, which is determined by station name. 
 */

import java.util.Random;

public class Event implements Comparable<Event>
{
	private Station eventStation; //station name 
	private double prodTime;
	private double endTime; //time event will finish 
	
	//default constructor 
	Event(){}
	
	//constructor taking station and eventhelper parameters 
	Event(Station s, EventHelper e) 
	{
		String n = s.getName(); //gets station name
		double simulationTime = e.getSimulationTime(); //gets the current time of simulation 
		generateProductionTime(n, e); //generates a production time based on station, mean and range 
		endTime = simulationTime + prodTime; //calulate end time using T2 = T1 + P
		s.updateProductionTime(prodTime); //update time spent in production for station 
		eventStation = s; //set station 
	}
	
	//method to calculate production time based on station name, mean and range 
	//PRE: must take string and eventhelper as parameters 
	//POST: calculates the production time
	private void generateProductionTime(String name, EventHelper e)
	{
		int m = e.getMean(); //gets the mean 
		int n = e.getRange(); //gets the range 
		
		//if station name matches one of these stations, double mean and range 
		if(name == "s2a" || name == "s2b" || name == "s4a" || name == "s4b")
		{
			m = 2 * m;
			n = 2 * n;
			prodTime = m + n * (e.getRandom() - 0.5); //using formula P = M + N * (D - 0.5)
		}
		//otherwise compute with base values 
		else 
			prodTime = m + n * (e.getRandom() - 0.5); //using formula P = M + N * (D - 0.5)
	}
	
	//method to set end time of event
	//PRE: must be double 
	//POST: end time is set 
    public void setEndTime(double t) 
	{
        endTime = t;
    }

	//method to return end time of event 
	//PRE: n/a 
	//POST: returns end time 
    public double getEndTime() 
	{
        return endTime;
    }

	//method to set event station 
	//PRE: must be Station
	//POST: station is set 
    public void setEventStation(Station s) 
	{
        eventStation = s;
    }

	//method to return event station 
	//PRE: n/a 
	//POST: station is returned 
    public Station getEventStation() 
	{
        return eventStation;
    }
	
	//override of compareTo function - used to determine Event priority in priority queue 
	@Override
    public int compareTo(Event e) 
	{
        if (endTime > e.getEndTime()) 
            return 1;
		else
            return -1;
    }
}