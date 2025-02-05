package org.js.swiftcodes.api;

import org.js.swiftcodes.api.model.BankData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwiftCodeController {

    @GetMapping("/v1/swift-codes/{swift-code}")
    public ResponseEntity<BankData> getAnimal(@PathVariable("swift-code") String swiftCode) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
