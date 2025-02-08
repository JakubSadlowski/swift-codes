package org.js.swiftcodes.api;

import lombok.extern.apachecommons.CommonsLog;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.api.model.Error;
import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.DeleteSwiftCodeService;
import org.js.swiftcodes.service.SingleSwiftCodeGetService;
import org.js.swiftcodes.service.exceptions.SwiftCodeInvalidException;
import org.js.swiftcodes.service.exceptions.SwiftCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/")
@CommonsLog
public class SwiftCodesController {
    private final SingleSwiftCodeGetService singleSwiftCodeGetService;
    private final DeleteSwiftCodeService deleteSwiftCodeService;
    private static final int SWIFT_CODE_LENGTH = 11;

    @Autowired
    public SwiftCodesController(SingleSwiftCodeGetService singleSwiftCodeGetService, DeleteSwiftCodeService deleteSwiftCodeService) {
        this.singleSwiftCodeGetService = singleSwiftCodeGetService;
        this.deleteSwiftCodeService = deleteSwiftCodeService;
    }

    @GetMapping("swift-codes/{swift-code}")
    public ResponseEntity<BankData> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        if (swiftCode.length() != SWIFT_CODE_LENGTH) {
            throw new BadRequestException(String.format("Parameter swiftCode %s is invalid. SWIFT Code can't be empty and must have %d characters.", swiftCode, SWIFT_CODE_LENGTH));
        }

        return ResponseEntity.ok(singleSwiftCodeGetService.getSwiftCode(swiftCode));
    }

    @GetMapping("swift-codes/country/{countryISO2code}")
    public ResponseEntity<List<BankData>> getAllSwiftCodesForSpecificCountry(@PathVariable("countryISO2code") String countryISO2Code) {
        return null;
    }

    @DeleteMapping("swift-codes/{swift-code}")
    public ResponseEntity<Map<String, String>> deleteSwiftCode(@PathVariable("swift-code") String swiftCode, @RequestParam String bankName, @RequestParam String countryISO2Code) {
        if (swiftCode.length() != SWIFT_CODE_LENGTH) {
            throw new BadRequestException(String.format("Parameter swiftCode %s is invalid. SWIFT Code can't be empty and must have %d characters.", swiftCode, SWIFT_CODE_LENGTH));
        }

        int deletedCount = deleteSwiftCodeService.deleteSwiftCode(swiftCode, bankName, countryISO2Code);

        Map<String, String> response = new HashMap<>();
        response.put("message", String.format("Successfully deleted %d record with SWIFT code: %s", deletedCount, swiftCode));

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(SwiftCodeNotFoundException.class)
    public ResponseEntity<Error> handleSwiftCodeNotFoundException(SwiftCodeNotFoundException ex, WebRequest request) {
        Error response = Error.of("NOT_FOUND", ex.getMessage());
        log.warn("Handled SwiftCodeNotFoundException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }

    @ExceptionHandler(SwiftCodeInvalidException.class)
    public ResponseEntity<Error> handleInvalidSwiftCodeException(Exception ex, WebRequest request) {
        Error response = Error.of("INVALID_INPUT", ex.getMessage());
        log.warn("Handled SwiftCodeInvalidException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }
}
