package org.ilri.mistro.ussd;

import java.util.ArrayList;
import java.util.List;


public class Farmer 
{
	private String name;
	private String county;
	private String district;
	private List<Cow> cows;
	
	public Farmer() 
	{
		cows=new ArrayList<Cow>();
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public void setCounty(String county)
	{
		this.county=county;
	}
	public void setDistrict(String district)
	{
		this.district=district;
	}
	public void addCow(Cow cow)
	{
		cows.add(cow);
	}
}
