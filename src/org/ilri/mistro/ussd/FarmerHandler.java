package org.ilri.mistro.ussd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import hms.sdp.ussd.client.MchoiceUssdSender;

public class FarmerHandler extends Handler
{
	public static final String KEY="testDayHandler";
	public static final int screen1Id=2322;
	private final String[] screens=new String[]
	        {"REGISTRATION\n\n What is your name?",
			"REGISTRATION\n\n Which County are you in?",
			"REGISTRATION\n\n Which District are you in?",
			"REGISTRATION\n\n How many cows do you have?",
			""};
	private final Farmer farmer;
	private int unregisteredCows;
	public FarmerHandler(MchoiceUssdSender ussdSender, String address, String conversationId, HashMap<String, Object> userData)
	{
		super(ussdSender,address,conversationId,userData);
		this.farmer=new Farmer();
	}
	
	public void showInitMenu()//show initial menu in this class
	{
		showMenu(screens[0], screen1Id);
	}
	
	public void handleMessage(String message)
	{
		String footprintText=getFootprintText();
		if(footprintText.equals(String.valueOf(screen1Id)))//message from first screen
		{
			farmer.setName(message);
			showMenu(screens[1], 1);
		}
		else if(footprintText.equals(String.valueOf(screen1Id)+","+String.valueOf(1)))//reply from second screen
		{
			farmer.setCounty(message);
			showMenu(screens[2], 2);
		}
		else if(footprintText.equals(String.valueOf(screen1Id)+","+String.valueOf(1)+","+String.valueOf(2)))//reply from third scree
		{
			farmer.setDistrict(message);
			showMenu(screens[3], 3);
		}
		else if(footprintText.equals(String.valueOf(screen1Id)+","+String.valueOf(1)+","+String.valueOf(2)+","+String.valueOf(3)))
		{
			unregisteredCows=Integer.valueOf(message);
			//TODO: call the cow handler until all cows are registerd
		}
	}

}
