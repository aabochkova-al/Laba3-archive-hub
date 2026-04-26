/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mission.laba3.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 *
 * @author aleksandra
 */
public class JsonParser extends BasicParser {
    private ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected Map<String, Object> parseToMap(String filepath) throws IOException{
      return mapper.readValue(new File(filepath), Map.class);
    }

    @Override
    public String getExtension() {
        return "json";
    }
    
}
