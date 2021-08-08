package restApiServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import restApiRepository.DAO;


@ComponentScan(basePackages = {"restApiRepository"} )
@Service
public class DataService {

	
	@Autowired
	DAO repository;
	
	public JSONArray getEntity(String entityType) {
		// TODO Auto-generated method stub
		JSONObject stores= repository.readDataFromJSONFile();  
		JSONArray arr = new JSONArray();
		arr = (JSONArray) stores.get(entityType);
		return arr;
	}

	
	public JSONObject getEntityById(String entityType, Long id)
	{
		JSONArray entityList = getEntity(entityType);
		JSONObject ans= new JSONObject();
		JSONObject temp;
		for(int i=0;i<entityList.size();++i)
		{
				temp = (JSONObject)entityList.get(i);
//				System.out.println(temp.toString());
				Long tempId = (Long)temp.get("id");
//				System.out.println(" The ttttempID is : "+tempId +" which is not equal to " + id);
				if(tempId.equals(id))
				{
//					System.out.println("Success");
					return temp;
				}
		}
		return ans;
	}
	
	
	public  JSONObject getEntityByFilter(String entityType, String title , String author)
	{
		JSONArray entityList = getEntity(entityType);
		JSONObject ans=new JSONObject();
		JSONObject temp;
		for(int i=0;i<entityList.size();++i)
		{
				temp = (JSONObject)entityList.get(i);
//				System.out.println(temp.toString());
				String tempTitle = (String)temp.get("title");
				String tempAuthor = (String)temp.get("author");
				if(tempTitle.equals(title) && tempAuthor.equals(author))
				{
//					System.out.println("Success");
					return temp;
				}
		}
		return ans;
	}
	

	@SuppressWarnings("unchecked")
	public  JSONArray getEntityFilterByTitle(String entityType, String title) {
		JSONArray entityList = getEntity(entityType);
		JSONArray ans = new JSONArray();
		JSONObject temp;
		for(int i=0;i<entityList.size();++i)
		{
				temp = (JSONObject)entityList.get(i);
//				System.out.println(temp.toString());
				String tempTitle = (String)temp.get("title");

				if(tempTitle.equals(title))
				{
//					System.out.println("Success");
//					return temp
					ans.add(temp);
				}
		}
		return ans;
	}
	
	@SuppressWarnings("unchecked")
	public  JSONArray getEntityFilterByAuthor(String entityType, String author) {
		JSONArray entityList = getEntity(entityType);
		JSONArray ans =new JSONArray();
		JSONObject temp;
		for(int i=0;i<entityList.size();++i)
		{
				temp = (JSONObject)entityList.get(i);
				System.out.println(temp.toString());
				String tempAuthor = (String)temp.get("author");
				if(tempAuthor.equals(author))
				{
					ans.add(temp);
				}
		}
		return ans;
	}

	
	@SuppressWarnings("unchecked")
	public JSONArray sortBySomeKey(String entityType , String key , String order)
	{
		JSONArray jsonArr =  getEntity(entityType);
	    JSONArray sortedJsonArray = new JSONArray();

	    List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	    for (int i = 0; i < jsonArr.size(); i++) {
	        jsonValues.add((JSONObject)jsonArr.get(i));
	    }
	    Collections.sort(jsonValues,new Sorting(key,order));
	    for (int i = 0; i < jsonArr.size(); i++) {
	        sortedJsonArray.add(jsonValues.get(i));
	    }
		return sortedJsonArray;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void postData(String entityType,JSONObject obj)
	{
		JSONObject stores= repository.readDataFromJSONFile(); 
		JSONArray arr = new JSONArray();
		arr = (JSONArray)stores.get(entityType);
		arr.add(obj);
		stores.put(entityType,arr);
		repository.writeDataToJSONFile(stores);
	}
	
	
	@SuppressWarnings("unchecked")
	public  void putData(String entityType,Long id, JSONObject object) {
		
		JSONObject stores= repository.readDataFromJSONFile();   
		JSONArray entityList = new JSONArray();		
		JSONObject temp;
		entityList = (JSONArray) stores.get(entityType);
		int updateIndex = -1;
		for(int i=0;i<entityList.size();++i)
		{
				temp = (JSONObject)entityList.get(i);
//				System.out.println(temp.toString());
				Long tempId = (Long)temp.get("id");
//				System.out.println(tempId);
				if(tempId.equals(id))
				{
//					System.out.println("Success");			
					updateIndex= i;
					break;
				}
		}
		if(updateIndex!=-1)
		{
			entityList.remove(updateIndex);
			entityList.add(updateIndex,object);
			stores.put(entityType, entityList);
			repository.writeDataToJSONFile(stores);
		}
	
	}
	
	
	@SuppressWarnings("unchecked")
	public  void patchData(String entityType, Long id,JSONObject object) {
		
		JSONObject stores=repository.readDataFromJSONFile(); 
		JSONArray arr = (JSONArray)stores.get(entityType);
		JSONObject temp;
		JSONArray entityList = getEntity(entityType);
		System.out.println(entityList.toString());
		
		for(int i=0;i<entityList.size();++i)
		{
				temp = (JSONObject)entityList.get(i);
				System.out.println(temp.toString());
				Long tempId = (Long)temp.get("id");
				if(tempId.equals(id))
				{
					String tempKey;
					for(Object key : object.keySet())
					{
						tempKey =(String) key;
						temp.put(tempKey,object.get(tempKey));
					}
					entityList.remove(i);
					entityList.add(i, temp);
					break;
				}
		}
		stores.put(entityType, entityList);
		repository.writeDataToJSONFile(stores);
	}
	
	@SuppressWarnings("unchecked")
	public  void deleteEntityById(String entityType, Long id)
	{
		JSONObject stores= repository.readDataFromJSONFile(); 
		JSONArray entityList=(JSONArray) stores.get(entityType);
		JSONObject temp;
		int deleteIndex=-1;
		for(int i=0;i<entityList.size();++i)
		{
				temp = (JSONObject)entityList.get(i);
				Long tempId = (Long)temp.get("id");
				if(tempId.equals(id))
				{
					deleteIndex = i;
					break;
				}
		}
		
		if(deleteIndex !=-1)
		{
			entityList.remove(deleteIndex);
		}
		
		stores.put(entityType,entityList);
		repository.writeDataToJSONFile(stores);
	
	}


	public boolean IdExists(String entityType, Long id) {
		
		JSONObject stores= repository.readDataFromJSONFile();  
		JSONArray arr = (JSONArray)stores.get(entityType);
		
//		if(arr.c)
		
		JSONObject temp;
		Long tempId;
		
		for(int i =0;i<arr.size();++i)
		{
			temp = (JSONObject) arr.get(i);
//			System.out.println(" id traversed is : " + id);
//			tempId = (Long)temp.get("id");
			tempId = ((Number)temp.get("id")).longValue();
			System.out.println(" id traversed is : " + tempId + " which is not equal to " + id);
			if(tempId.equals(id))
			{
				return true;
			}
		}			
		return false;
	}


	public boolean keyExists(String entityType, String key) {
		
		JSONObject stores= repository.readDataFromJSONFile();  
		JSONArray arr = (JSONArray) stores.get(entityType);
		JSONObject obj = (JSONObject)arr.get(0);
		return	obj.containsKey(key);
	}


	@SuppressWarnings("unchecked")
	public void postEntitySet(JSONObject object) {
		JSONObject stores= repository.readDataFromJSONFile();  
		
		for(Object key : object.keySet())
		{
			String tempKey =(String) key;
			stores.put(tempKey,object.get(tempKey));
		}
//		stores.put(entityType, arr);
		repository.writeDataToJSONFile(stores);
	}


	public JSONObject getStore() {
		JSONObject stores= repository.readDataFromJSONFile();  
		return stores;
		// TODO Auto-generated method stub
//		return null;
	}

	
	
	@SuppressWarnings("unchecked")
	public void search(JSONObject object, JSONArray ans, String query)
	{
		
		String tempKey;
		String tempValue;
		
		for(Object key : object.keySet())
		{
			tempKey =(String) key;
			if(object.get(tempKey) instanceof String)
			{
				
				tempValue= (String)object.get(tempKey);
				System.out.println(tempValue);
				if(tempValue.equalsIgnoreCase(query))
				{
					JSONObject obj =new JSONObject();
					obj.put(tempKey, tempValue);
					ans.add(obj);
				}
			}
			else if(object.get(tempKey) instanceof JSONObject)
			{
				search((JSONObject)object.get(tempKey),ans,query);
			}
			else if(object.get(tempKey) instanceof JSONArray)
			{
				JSONArray arr = new JSONArray();
				
				arr = (JSONArray) object.get(tempKey);
				
				for(int i =0;i<arr.size();++i)
				{
					search((JSONObject)arr.get(i),ans,query);
				}
			
			}
		}
		
	}

	public JSONArray searchQuery(String entityType, String query) {
		
		
		JSONArray arr = getEntity(entityType);
		
		JSONObject object;
		
		JSONArray ans  =  new JSONArray();
		for(int i  =0; i<arr.size();++i)
		{
			object  = (JSONObject)arr.get(i);
			search(object,ans,query);	
		}
		
		return ans;
	}


	public void emptyJSONFile() {
		
		JSONObject object = new JSONObject();
		repository.writeDataToJSONFile(object);
		
	}
	

	
}
