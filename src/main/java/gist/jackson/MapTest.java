package gist.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gist.jackson.support.AnyDTO;
import gist.jackson.support.PropertyMap;

import java.util.Map;

public class MapTest {

    public static void main(String[] args) throws JsonProcessingException {
        new MapTest()
                .testMap()
                .testMapDTO();
    }

    static Map<String,?> data() {
        return Map.of("AQO_ACCT_NUM", "001", "XQO_CUST_NUM", "Prashant", "TCA_AMT", 10000);
    }

    private MapTest testMap() throws JsonProcessingException {
        json(data());
        return this;
    }

    AnyDTO test2Data() {
        PropertyMap data = new PropertyMap();
        data.setId(100l);
        data.setName("foo");
        data.putAll(data());
        return data;
    }

    private MapTest testMapDTO() throws JsonProcessingException {
        json(test2Data());
        return this;
    }

    void json(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        System.out.println(json);
    }
}
