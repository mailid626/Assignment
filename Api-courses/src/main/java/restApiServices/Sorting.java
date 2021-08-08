package restApiServices;

import java.util.Comparator;

import org.json.simple.JSONObject;

public class Sorting implements Comparator<JSONObject> {
	
// string , number , boolean , array , object 
	private  String key;
	private  String order;
	public Sorting(String key, String order) {
		this.key = key;
		this.order = order;
	}
	
	@Override
	public int compare(JSONObject a, JSONObject b) {
		 Object valA = a.get(key);
         Object valB = b.get(key);
         if(order.equals("asc"))
         {
        	if(valA instanceof Number)
        	{
        		Long A = (Long)valA;
        		Long B = (Long)valB;
        		return A.compareTo(B);
        	}
        	else 
        	{
        		String A = (String)valA;
        		String B = (String)valB;
        		return A.compareTo(B);
        	}

        }
        else
        {
        	if(valA instanceof Number)
        	{
        		Long A = (Long)valA;
        		Long B = (Long)valB;
        		return -A.compareTo(B);
        	}
        	else 
        	{
        		String A = (String)valA;
        		String B = (String)valB;
        		return -A.compareTo(B);
        	}

        }
	}
	
//	@Override
//	public int compare(JSONObject a, JSONObject b) {
//		
//		String valA = new String();
//        String valB = new String();
//        valA = (String) a.get(key);
//        valB = (String) b.get(key);
//        return valA.compareTo(valB);
//		
//	}


}
