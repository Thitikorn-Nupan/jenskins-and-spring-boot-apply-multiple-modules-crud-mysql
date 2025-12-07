package com.ttknp.api.controller;

import com.ttknp.api.annotation.CommonRestAPI;
import com.ttknp.api.constant.CommonStatus;
import com.ttknp.api.entity.ResponseObject;
import com.ttknp.api.entity.Romance;
import com.ttknp.api.repository.ModelRepository;
import com.ttknp.api.validates.ValidateHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CommonRestAPI(configPath = "/api/romance", configOrigins = "*")
public class RomanceController {

    private final ModelRepository<Romance> repository;

    @Autowired
    public RomanceController(ModelRepository<Romance> repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/selectAll")
    private ResponseEntity<ResponseObject<?>> retrieveAllModels() {
        return ResponseEntity
                .status((Short) CommonStatus.OK[0])
                .body(ResponseObject.builder()
                        .status((Short) CommonStatus.OK[0])
                        .info((String) CommonStatus.OK[1])
                        .data(repository.retrieveAllModels())
                        .build()
                );
    }

    @GetMapping(value = "/selectOne",params = {"pk","!rid"}) // !rid parameter is not present in the request
    private ResponseEntity<ResponseObject<?>> retrieveAllModel(@RequestParam String pk) {
        return ResponseEntity
                .status((Short) CommonStatus.OK[0])
                .body(ResponseObject.builder()
                        .status((Short) CommonStatus.OK[0])
                        .info((String) CommonStatus.OK[1])
                        .data(repository.retrieveModel(pk))
                        .build()
                );
    }

    @PostMapping(value = "/insertOne")
    private ResponseEntity<ResponseObject<?>> createModel(@RequestBody Romance romance) {
        return ResponseEntity
                .status((Short) CommonStatus.CREATE[0])
                .body(ResponseObject.builder()
                        .status((Short) CommonStatus.CREATE[0])
                        .info((String) CommonStatus.CREATE[1])
                        .data(repository.createModel(romance))
                        .build()
                );
    }

    @PutMapping(value = "/updateOne",params = {"pk","!rid"}) // !rid parameter is not present in the request
    private ResponseEntity<ResponseObject<?>> updateModel(@RequestBody Romance romance,@RequestParam String pk) {
        if (ValidateHelperService.isNotEmptyString(pk)){
            romance.setRid(pk);
        }
        return ResponseEntity
                .status((Short) CommonStatus.ACCEPTED[0])
                .body(ResponseObject.builder()
                        .status((Short) CommonStatus.ACCEPTED[0])
                        .info((String) CommonStatus.ACCEPTED[1])
                        .data(repository.updateModel(romance))
                        .build()
                );
    }

    @DeleteMapping(value = "/deleteOne",params = {"pk","!rid"}) // !rid parameter is not present in the request
    private ResponseEntity<ResponseObject<?>> deleteModel(@RequestParam String pk) {
        return ResponseEntity
                .status((Short) CommonStatus.ACCEPTED[0])
                .body(ResponseObject.builder()
                        .status((Short) CommonStatus.ACCEPTED[0])
                        .info((String) CommonStatus.ACCEPTED[1])
                        .data(repository.deleteModel(pk))
                        .build()
                );
    }
}
