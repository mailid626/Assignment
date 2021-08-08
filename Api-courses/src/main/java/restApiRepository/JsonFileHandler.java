package restApiRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class JsonFileHandler implements DAO{

//	String path ="C:\\Users\\lappy\\Desktop\\stores.json";
	
	String path ="/home/ashar786khan/stores.json";
	
	@Override
	public JSONObject readDataFromJSONFile() {
		
		JSONParser jsonParser = new JSONParser();
	    JSONObject stores = new JSONObject();
//	    InputStream input = getClass().getResourceAsStream("sample.json");
		System.out.println("Reading file from "+path);
	    
	    try  {
            // Read JSON file
	    	FileReader reader = new FileReader(path);
	    	Object obj = jsonParser.parse(reader);
             stores= (JSONObject) obj;
        }
        catch (FileNotFoundException e) {
            System.out.println(" The file does not exist. ");
        }
	    catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }   
        return stores;
	}

	@Override
	public void writeDataToJSONFile(JSONObject object) {
		
		try  {
		  
            Files.write(Paths.get(path),object.toJSONString().getBytes());
		
		} catch (Exception e) {
        
            e.printStackTrace();
        }
		
	}

}
