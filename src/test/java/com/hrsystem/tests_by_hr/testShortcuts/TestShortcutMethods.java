package com.hrsystem.tests_by_hr.testShortcuts;

import com.hrsystem.utilities.interfaces.IdOwner;
import org.json.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;

public class TestShortcutMethods<T extends IdOwner> {
    public void setObjectIdFromResponseResult(MvcResult result, T objectWithId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        String resultContent = result.getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(resultContent);
        Long idFromDB = jsonObject.getLong("id");

        objectWithId.setId(idFromDB); // expected Result

    }

    public void compareIdOwnerWithDataBase(T expectedResult, JpaRepository<T, Long> repository) throws Exception {
        IdOwner objectFromDb = repository.findById(expectedResult.getId()).orElse(null);
        if (objectFromDb == null)
            throw new Exception("didnt find it in DB");
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals(objectMapper.writeValueAsString(expectedResult), objectMapper.writeValueAsString(objectFromDb));

    }

}
