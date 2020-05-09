package gist.json;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

import static gist.filerepo.FileRepository.getFile;

class JSONFlatten {

    @Test
    void flatten() {

        JSONParser parser = new JSONParser();
        try {
            File file = getFile("src/test/java/gist/json/file.json");
            System.out.println( "file=" + file);

            Object obj = parser.parse(new FileReader(file));
            //Object obj = parser.parse(new FileReader("C:\\Users\\psurti\\git\\gist\\src\\test\\java\\gist\\file.json"));
            JSONObject jsonObject = (JSONObject) obj;

            // JsonFlattener: A Java utility used to FLATTEN nested JSON objects
            String flattenedJson = JsonFlattener.flatten(jsonObject.toString());
            //log("\n=====Simple Flatten===== \n" + flattenedJson);

            Map<String, Object> flattenedJsonMap = JsonFlattener.flattenAsMap(jsonObject.toString());

            log("\n=====Flatten As Map=====\n" + flattenedJson);
            // We are using Java8 forEach loop. More info: https://crunchify.com/?p=8047
            String prefix = "export SAS_FRAUDTRANSACTION_";
            flattenedJsonMap.forEach((k, v) -> log( prefix + k.replace('.', '_').toUpperCase().replace('[', '_').replace(']', '_') + "=\"" + v +"\""));

            // Unflatten it back to original JSON
            String nestedJson = JsonUnflattener.unflatten(flattenedJson);
            //System.out.println("\n=====Unflatten it back to original JSON===== \n" + nestedJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void log(String flattenedJson) {
        System.out.println(flattenedJson);
    }
}