package org.ilri.mistro.ussd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import hms.sdp.ussd.MchoiceUssdException;
import hms.sdp.ussd.MchoiceUssdMessage;
import hms.sdp.ussd.MchoiceUssdTerminateMessage;
import hms.sdp.ussd.client.MchoiceUssdReceiver;
import hms.sdp.ussd.client.MchoiceUssdSender;


public class MessageReciever extends MchoiceUssdReceiver
{
	private final String mainMenuText="MISTRO\n 1:Register farmer\n 2:Register cow\n 98:Exit";
	private final String sessionTerminationText="Session terminated";
	private ConcurrentMap<String, HashMap<String, Object>> session=new ConcurrentHashMap<String, HashMap<String, Object>>();
	
	@Override
	public void onMessage(MchoiceUssdMessage arg0)
	{
		String message = arg0.getMessage();
        String address = arg0.getAddress();
        String conversationId = arg0.getConversationId();
        String correlationId = arg0.getCorrelationId();
		try 
		{
			MchoiceUssdSender ussdSender=new MchoiceUssdSender("http://127.0.0.1:8000/ussd/", "appid", "password");
			if(session.containsKey(address))//not the first time for user
			{
				HashMap<String, Object> userData=session.get(address);
				ArrayList<Integer> userFootprint=(ArrayList<Integer>)userData.get(Handler.FOOTPRINT_KEY);
				if(userFootprint.size()==0)//from the main menu
				{
					if(message.equals(String.valueOf(FarmerHandler.screen1Id)))
					{
						FarmerHandler farmerHandler=(FarmerHandler)userData.get(FarmerHandler.KEY);
						farmerHandler.setUserData(userData);
						farmerHandler.showInitMenu();
					}
				}
				else
				{
					if(userFootprint.get(0)==FarmerHandler.screen1Id)
					{
						FarmerHandler farmerHandler=(FarmerHandler)userData.get(FarmerHandler.KEY);
						farmerHandler.handleMessage(message);
					}
				}
			}
			else//first time for this user
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put(Handler.FOOTPRINT_KEY, new ArrayList<Integer>());
				hashMap.put(FarmerHandler.KEY, new FarmerHandler(ussdSender,address, conversationId,null));
				session.put(address, hashMap);
				ussdSender.sendMessage(mainMenuText, address, conversationId, false);
			}
		} 
		catch (MchoiceUssdException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void terminateSession(MchoiceUssdSender ussdSender, boolean showTerminationText, String address, String conversationId)
	{
		try 
		{
			if(showTerminationText)
			{
				ussdSender.sendMessage(sessionTerminationText, address, conversationId, true);
			}
			session.remove(address);
		} 
		catch (MchoiceUssdException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onSessionTerminate(MchoiceUssdTerminateMessage arg0) 
	{
		session.remove(arg0.getAddress());
	}
	

}