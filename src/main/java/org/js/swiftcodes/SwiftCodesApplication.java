package org.js.swiftcodes;

import org.js.swiftcodes.service.reader.SwiftCodesReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwiftCodesApplication {

    public static void main(String[] args) throws Exception {
        SwiftCodesReader.readSwiftCodesFile("src/main/resources/Interns_2025_SWIFT_CODES.xlsx");
        SpringApplication.run(SwiftCodesApplication.class, args);
    }
}
