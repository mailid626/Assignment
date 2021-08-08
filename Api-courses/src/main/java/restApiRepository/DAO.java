package restApiRepository;

import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;


public interface DAO {
	public JSONObject readDataFromJSONFile();
	public void writeDataToJSONFile(JSONObject object);
	
}