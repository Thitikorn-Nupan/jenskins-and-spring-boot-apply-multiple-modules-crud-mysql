package com.ttknp.api;

import com.ttknp.api.entity.Romance;
import com.ttknp.api.repository.ModelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ApiServiceLayerTests {

    // It's necessary ?
    // @InjectMocks
    // private JdbcSelectHelper<Romance> jdbcSelectHelper;
    // @InjectMocks
    // private JdbcInsertUpdateDeleteHelper<Romance> jdbcInsertUpdateDeleteHelper;
    // @InjectMocks
    // private JdbcTemplate jdbcTemplate;
    @Mock
    private ModelRepository<Romance> modelRepository;

    @Test
    public void testRetrieveAllModels() {
        // Note this not normal concept but it works with multiple module
        /*
            So, we have to tell Mockito to return something when userRepository.findAll() is called.
            We do this with the static when method.
        */
        Mockito.when(modelRepository.retrieveAllModels()).thenReturn(getRomances());
        /*
            Now users will call employeeLayerService.reads() it means. employeeRepository.findAll() method was called
        */
        List<Romance> romances = modelRepository.retrieveAllModels();
        Assertions.assertEquals(3, romances.size());
        Mockito.verify(modelRepository, Mockito.times(1)).retrieveAllModels();
    }

    @Test
    public void testRetrieveModel() {
        String uuid = "26814113-540f-41f2-950e-6d3cd97a7e23";
        Mockito.when(modelRepository.retrieveModel(uuid)).thenReturn(getRomances().get(0));
        Romance romance = modelRepository.retrieveModel(uuid);
        Assertions.assertEquals(uuid, romance.getRid());
        Mockito.verify(modelRepository, Mockito.times(1)).retrieveModel(uuid);
    }

    @Test
    public void testCreateModel() {
        String uuid = "43814113-540f-41f2-950e-6d3cd97a7e23";
        Romance romanceNew = getRomances().get(0);
        romanceNew.setRid(uuid);
        Mockito.when(modelRepository.createModel(romanceNew)).thenReturn(true);
        Boolean romanceRes = modelRepository.createModel(romanceNew);
        Assertions.assertEquals(true, romanceRes);
        Mockito.verify(modelRepository, Mockito.times(1)).createModel(romanceNew);
    }

    @Test
    public void testUpdateModel() {
        Romance romanceNew = getRomances().get(0);
        romanceNew.setTitle("new title");
        Mockito.when(modelRepository.updateModel(romanceNew)).thenReturn(true);
        Boolean romanceRes = modelRepository.updateModel(romanceNew);
        Assertions.assertEquals(true, romanceRes);
        Mockito.verify(modelRepository, Mockito.times(1)).updateModel(romanceNew);
    }

    @Test
    public void testDeleteModel() {
        String uuid = "43814113-540f-41f2-950e-6d3cd97a7e23";
        Mockito.when(modelRepository.deleteModel(uuid)).thenReturn(true);
        Boolean romanceRes = modelRepository.deleteModel(uuid);
        Assertions.assertEquals(true, romanceRes);
        Mockito.verify(modelRepository, Mockito.times(1)).deleteModel(uuid);
    }


    private List<Romance> getRomances() {
        return List.of(
          new Romance("26814113-540f-41f2-950e-6d3cd97a7e23","title",0),
          new Romance(),
          new Romance()
        );
    }
}
