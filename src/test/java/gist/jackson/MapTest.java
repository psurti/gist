package gist.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gist.jackson.support.AnyDTO;
import gist.jackson.support.PropertyMap;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class MapTest {

    static Map<String,?> data() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("AQO_ACCT_NUM", "001");
        map.put("XQO_CUST_NUM", "Prashant");
        map.put("TCA_AMT", 10000);
        return map;
    }

    @Test
    void testMap() throws JsonProcessingException {
        json(data());
    }

    AnyDTO test2Data() {
        PropertyMap data = new PropertyMap();
        data.setId(200L);
        data.setName("foo");
        data.putAll(data());
        return data;
    }

    @Test
    void  testMapDTO() throws JsonProcessingException {
        json(test2Data());
    }

    void json(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        System.out.println(json);
    }

}