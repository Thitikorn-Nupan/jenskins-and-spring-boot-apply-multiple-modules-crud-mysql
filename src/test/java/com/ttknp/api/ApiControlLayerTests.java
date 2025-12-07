package com.ttknp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttknp.api.controller.RomanceController;
import com.ttknp.api.entity.Romance;
import com.ttknp.api.repository.ModelRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Note If the bean you're trying to inject from a JAR file is not an MVC-specific component (e.g., it's a service or a utility class),
 *
 * @WebMvcTest will not include it in the application context created for the test.
 * But you can still inject all the beans on jar file
 * You have to comment @ComponentScan(basePackages = {"com.ttknp"}) on your main class for testing mode
 */
// *** JUnit5 test cases for CRUD REST APIs. use the @WebMvcTest annotation to load only UserController class. (can multiple rest controller)
@WebMvcTest(RomanceController.class)
public class ApiControlLayerTests {
    // *** using MockMvc class to make REST API calls.
    @Autowired
    private MockMvc mockMvc;
    // *** using @MockitoBean annotation to add mock objects to the Spring application context. The mock will replace any existing bean of the same type in the application context.
    @MockitoBean
    private ModelRepository<Romance> modelRepository;

    @Test
    public void testSelectAll() throws Exception {
        /// ** provide response if service calls ** it's kinda same as when(...).return(...)
        // given - precondition or setup
        given(modelRepository.retrieveAllModels()).willReturn(getRomances());
        /// ** call the provider **
        // when -  action or the behaviour(n.พฤติกรรม) that we are going test
        RequestBuilder request = MockMvcRequestBuilders.get("/api/romance/selectAll"); // Note i have to cut /api because this prefix work after module running
        // ** ResultActions class to handle the response of the REST API.
        ResultActions response = mockMvc.perform(request);
        ///  ** result follow your api response
        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data[0].rid").value("26814113-540f-41f2-950e-6d3cd97a7e23"))
                .andExpect(jsonPath("$.data[0].title").value("title"));
    }

    @Test
    public void testSelectOne() throws Exception {
        given(modelRepository.retrieveModel("26814113-540f-41f2-950e-6d3cd97a7e23")).willReturn(getRomances().get(0));
        RequestBuilder request = MockMvcRequestBuilders.get("/api/romance/selectOne")
                .param("pk","26814113-540f-41f2-950e-6d3cd97a7e23");
        ResultActions response = mockMvc.perform(request);
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.rid").value("26814113-540f-41f2-950e-6d3cd97a7e23"))
                .andExpect(jsonPath("$.data.title").value("title"));
    }

    @Test
    public void testInsertOne() throws Exception {
        Romance romance = getRomances().get(0);
        romance.setRid(null);
        /// ** provide response if service calls ** it's kinda same as when(...).return(...)
        // given - precondition or setup
        given(modelRepository.createModel(romance)).willReturn(true);

        // convert java to json as string
        String requestBody = new ObjectMapper().writeValueAsString(romance);

        /// ** call the provider **
        // when -  action or the behaviour(n.พฤติกรรม) that we are going test
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/romance/insertOne")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody); // Note i have to cut /api because this prefix work after module running
        // ** ResultActions class to handle the response of the REST API.
        ResultActions response = mockMvc.perform(request);
        ///  ** result follow your api response
        // then - verify the output
        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testUpdateOne() throws Exception {
        Romance romance = getRomances().get(0);
        romance.setTitle("update title");
        given(modelRepository.updateModel(romance)).willReturn(true);
        String requestBody = new ObjectMapper().writeValueAsString(romance);
        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/romance/updateOne")
                .param("pk",romance.getRid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);
        ResultActions response = mockMvc.perform(request);
        response.andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void testDeleteOne() throws Exception {
        given(modelRepository.deleteModel("26814113-540f-41f2-950e-6d3cd97a7e23")).willReturn(true);
        RequestBuilder request = MockMvcRequestBuilders
                .delete("/api/romance/deleteOne")
                .param("pk","26814113-540f-41f2-950e-6d3cd97a7e23");
        ResultActions response = mockMvc.perform(request);
        response.andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").value(true));
    }

    private List<Romance> getRomances() {
        return List.of(
                new Romance("26814113-540f-41f2-950e-6d3cd97a7e23","title",0),
                new Romance(),
                new Romance()
        );
    }
}
