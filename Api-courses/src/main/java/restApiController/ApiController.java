package restApiController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import restApiServices.DataService;

@ComponentScan(basePackages = {"restApiServices"} )
@RestController
public class ApiController {
	
		@Autowired
		DataService service;
		
		
		/*
		 * 
		 * GET MAPPINGS
		 * 
		 */
		
		// Welcome Page
		@GetMapping(value = "/")
		public ResponseEntity<Object> HomePage()
		{
			return new ResponseEntity<>("Welcome to the application",HttpStatus.OK);
		}
		
		
		//Returns all the content of the JSON file 
		@GetMapping(value = "/jsonStore")
		public ResponseEntity<Object> getEntity()
		{
			return new ResponseEntity<>(service.getStore(),HttpStatus.OK);
		}
		
		
		// Returns all the content of the given entityType 
		@GetMapping(value = "/jsonStore/{entityType}")
		public ResponseEntity<Object> getEntity(@PathVariable String entityType)
		{
			JSONArray obj = service.getEntity(entityType);
			if(Objects.isNull(obj) || obj.isEmpty())
			{
				return new ResponseEntity<>("The entity "+entityType +" does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			else
			{
				return new ResponseEntity<>(obj,HttpStatus.OK);
			}
		}
		
		
		// Returns the content of the given entityType and id 
		@GetMapping("/jsonStore/{entityType}/{id}")
		public ResponseEntity<Object> getEntityById(@PathVariable String entityType, @PathVariable Long id)
		{

			JSONObject obj = service.getEntityById(entityType, id);
			if(Objects.isNull(obj) || obj.isEmpty())
			{
				return new ResponseEntity<>("The id "+ id +" does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			else
			{
				return new ResponseEntity<>(obj,HttpStatus.OK);
			}
		}
		  
		
		// Returns the content of the given entityType filtered by title and author
		//ASSUMPTION MADE :- The combination of tite and author will be unique
		@GetMapping(value = "/jsonStore/{entityType}", params = {"title","author"} )
		public ResponseEntity<Object> getEntityFilterByTitleAndAuthor(@PathVariable String entityType, @RequestParam("title") String title, @RequestParam("author") String author)
		{
			JSONObject obj = service.getEntityByFilter(entityType, title ,author);
			if(Objects.isNull(obj) || obj.isEmpty())
			{
				return new ResponseEntity<>(entityType + " with title "+ title +" and author " + author + " does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			else
			{
				return new ResponseEntity<>(obj,HttpStatus.OK);
			}

		}
		
		// Returns the content of the given entityType filtered by title
		@GetMapping(value = "/jsonStore/{entityType}", params = {"title"} )
		public  ResponseEntity<Object> getEntityFilterByTitle(@PathVariable String entityType, @RequestParam("title") String title)
		{	

			JSONArray obj = service.getEntityFilterByTitle(entityType, title);
			if(Objects.isNull(obj) || obj.isEmpty())
			{
				return new ResponseEntity<>(entityType + " with title "+ title + " does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			else
			{
				return new ResponseEntity<>(obj,HttpStatus.OK);
			}
		}
		
		
		// Returns the content of the given entityType filtered by author
		@GetMapping(value = "/jsonStore/{entityType}", params = {"author"})
		public  ResponseEntity<Object> getEntityFilterByAuthor(@PathVariable String entityType, @RequestParam("author") String author)
		{

			JSONArray obj = service.getEntityFilterByAuthor(entityType, author);
			if(Objects.isNull(obj) || obj.isEmpty())
			{
				return new ResponseEntity<>(entityType + " with author " + author + " does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			else
			{
				return new ResponseEntity<>(obj,HttpStatus.OK);
			}
		}
		
		
		// Sort the content of the given entityType based on the give key in ascending or descending order
		@GetMapping(value = "/jsonStore/{entityType}", params = {"_sort" , "_order"})
		public ResponseEntity<Object> getEntitySortedByViews(@PathVariable String entityType, @RequestParam("_sort") String key, @RequestParam("_order") String order)
		{
			
			if(!(order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("desc")) ||!service.keyExists(entityType,key))
			{
				return new ResponseEntity<>(" The Request Parameters are incorrect.",HttpStatus.BAD_REQUEST);
			}
			else
			{
				JSONArray obj = service.sortBySomeKey(entityType, key, order);
				return new ResponseEntity<>(obj,HttpStatus.OK);
			}
		
		}
		
		
		// Search the query in the nested DataSet and return the set of {key,value} pair  
		@GetMapping(value = "/jsonStore/search/{entityType}", params = {"query"})
		public  ResponseEntity<Object> searchEntity(@PathVariable String entityType, @RequestParam("query") String query)
		{
//			System.out.println("I am here");
			JSONArray obj = service.searchQuery(entityType, query);
			
			if(Objects.isNull(obj) || obj.isEmpty())
			{
//				System.out.println("");
				return new ResponseEntity<>(query + " does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			else
			{
				return new ResponseEntity<>(obj,HttpStatus.OK);
			}
		}
		
		
		/*
		 * 
		 * POST MAPPINGS
		 * 
		 */
		

		// Add a new dataset of new entityType to the JSON FILE
		@PostMapping("/jsonStore")
		public ResponseEntity<Object>  postDataTo(@RequestBody JSONObject obj)
		{
			service.postEntitySet(obj);
			return new ResponseEntity<>("Successful",HttpStatus.CREATED);
		}
		
		
		// Append data to an existing dataset of an entityType
		@PostMapping("/jsonStore/{entityType}")
		public ResponseEntity<Object>  postData(@RequestBody JSONObject object , @PathVariable String entityType)
		{
			if(Objects.isNull(object.get("id")))
			{
				return new ResponseEntity<>("The Object does not have a primary key ",HttpStatus.BAD_REQUEST);
			}
			long receivedId = ((Number)object.get("id")).longValue();	
			if(service.IdExists(entityType, receivedId))
			{
				return new ResponseEntity<>("The Object with the given Id already exists.",HttpStatus.BAD_REQUEST);
			}
		
			JSONArray temp = service.getEntity(entityType);
			if(Objects.isNull(temp) || temp.isEmpty())
			{
				return new ResponseEntity<>("The entity "+entityType +" does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			service.postData(entityType,object);
			return new ResponseEntity<>("Successful",HttpStatus.CREATED);
		}
	
		
		
		

		/*
		 * 
		 * PUT MAPPINGS
		 * 
		 */
		
		
		// Update data based on entityType with primary key id
		@PutMapping("/jsonStore/{entityType}/{id}")
		public  ResponseEntity<Object>  updateEntityById(@RequestBody JSONObject object, @PathVariable String entityType , @PathVariable Long id)
		{
			if(Objects.isNull(object.get("id")))
			{
				return new ResponseEntity<>("The Object does not have a primary key ",HttpStatus.BAD_REQUEST);
			}
			Long receivedId = ((Number)object.get("id")).longValue();
			if(!(receivedId.equals(id)))
			{
				return new ResponseEntity<>("The primary key cannot be changed",HttpStatus.BAD_REQUEST);
			}
			
			JSONArray temp = service.getEntity(entityType);
			if(Objects.isNull(temp) || temp.isEmpty())
			{
				return new ResponseEntity<>("The entity "+entityType +" does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			
			if(!service.IdExists(entityType, id))
			{
				return new ResponseEntity<>("The object with the given Id does not exist.",HttpStatus.NOT_FOUND);
			}
			
			
			service.putData(entityType,id,object);
			//return 204 NO Content 
			return new ResponseEntity<>("Update operation done successfully.",HttpStatus.NO_CONTENT);
		}
		
		
		/*
		 * 
		 * PATCH MAPPINGS
		 * 
		 */
		
		
		// Update data based on entityType with primary key id
		@PatchMapping("/jsonStore/{entityType}/{id}")
		public  ResponseEntity<Object>  patchEntityById(@RequestBody JSONObject object, @PathVariable String entityType , @PathVariable Long id)
		{	
			if(Objects.nonNull(object.get("id")))
			{
				Long receivedId = ((Number)object.get("id")).longValue();
				if(!(receivedId.equals(id)))
				{
					return new ResponseEntity<>("The primary key cannot be changed",HttpStatus.BAD_REQUEST);
				}
				
			}
			
			JSONArray temp = service.getEntity(entityType);
			if(Objects.isNull(temp) || temp.isEmpty())
			{
				return new ResponseEntity<>("The entity "+entityType +" does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
			
			if(!service.IdExists(entityType,id))
			{
				return new ResponseEntity<>("Id does not exist",HttpStatus.NOT_FOUND);
			}
			
			service.patchData(entityType,id,object);
			// return 204 No Content 
			return  new ResponseEntity<>("Update operation done successfully.",HttpStatus.NO_CONTENT);
		}
		
		
		
		/*
		 * 
		 * Delete MAPPINGS
		 * 
		 */
			
		
		// Delete data based on entityType with primary key id
		@DeleteMapping(value = "/jsonStore/{entityType}/{id}")
		public ResponseEntity<String> deleteTopic(@PathVariable String entityType, @PathVariable Long id)
		{	
			if(!service.IdExists(entityType,id))
			{
				return new ResponseEntity<>("Id does not exist",HttpStatus.NOT_FOUND);
			}
			JSONArray temp = service.getEntity(entityType);
			if(Objects.isNull(temp) || temp.isEmpty())
			{
				return new ResponseEntity<>("The entity "+entityType +" does not exist in the JSON File",HttpStatus.NOT_FOUND);
			}
//			JsonHandler.deleteEntityById(entityType, id);
			service.deleteEntityById(entityType, id);
			return new ResponseEntity<String>("Delete operation done successfully",HttpStatus.NO_CONTENT);
		
		}
		
		
		// empty the JSON file
		@DeleteMapping(value = "/jsonStore")
		public ResponseEntity<String> emptyJSONFile()
		{	
			service.emptyJSONFile();
			return new ResponseEntity<String>("Delete operation done successfully",HttpStatus.NO_CONTENT);	
		}
		
	
		
}
