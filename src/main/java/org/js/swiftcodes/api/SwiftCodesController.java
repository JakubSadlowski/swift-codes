package org.js.swiftcodes.api;

import lombok.extern.apachecommons.CommonsLog;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.api.model.Error;
import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.SingleSwiftCodeGetService;
import org.js.swiftcodes.service.exceptions.SwiftCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestController
@RequestMapping("/v1/")
@CommonsLog
public class SwiftCodesController {
    private final SingleSwiftCodeGetService singleSwiftCodeGetService;
    private static final int SWIFT_CODE_LENGTH = 11;

    @Autowired
    public SwiftCodesController(SingleSwiftCodeGetService singleSwiftCodeGetService) {
        this.singleSwiftCodeGetService = singleSwiftCodeGetService;
    }

    @GetMapping("swift-codes/{swift-code}")
    public ResponseEntity<BankData> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        if (swiftCode.length() != SWIFT_CODE_LENGTH) {
            throw new BadRequestException(String.format("Parameter swiftCode %s is invalid. SWIFT Code can't be empty and must have %d characters.", swiftCode, SWIFT_CODE_LENGTH));
        }

        return ResponseEntity.ok(singleSwiftCodeGetService.getSwiftCode(swiftCode));
    }

    @GetMapping("swift-codes/country/{countryISO2Code}")
    public ResponseEntity<List<BankData>> getAllSwiftCodesForSpecificCountry(@PathVariable("countryISO2Code") String countryISO2Code) {
        return null;
    }

    @ExceptionHandler(SwiftCodeNotFoundException.class)
    public ResponseEntity<Error> handleSwiftCodeNotFoundException(SwiftCodeNotFoundException ex, WebRequest request) {
        Error response = Error.of("NOT_FOUND", ex.getMessage());
        log.warn("Handled SwiftCodeNotFoundException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }
}
