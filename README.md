# swift-codes-rest-service

#Overview

A **SWIFT** code, also known as a Bank Identifier Code (BIC),
is a unique identifier of a bank's branch or headquarter.
It ensures that international wire transfers are directed to the correct bank and branch,
acting as a bank's unique address within the global financial network.
This service is used to manage **SWIFT** codes and serve them to the others.

## Environments

| Environment | Address                 |
|-------------|-------------------------|
| DEV         | https://localhost:8080/ |

# Swagger API live documentation

| Environment | Address                                |
|-------------|----------------------------------------|
| DEV         | https://localhost:8080/swagger-ui.html |

# API Endpoints and methods

| URIs                                        | Summary                                         | GET                | PUT                | POST | DEL                |
|---------------------------------------------|-------------------------------------------------|--------------------|--------------------|------|--------------------|
| `/v1/swift-codes/{swift-code}`              | Returns bank info for given SWIFT code          | :heavy_check_mark: |                    |      |                    |
| `/v1/swift-codes/country/{countryISO2code}` | Returns SWIFT code banks info for given country | :heavy_check_mark: |                    |      |                    |
| `/v1/swift-codes/`                          | Add SWIFT code bank info                        |                    | :heavy_check_mark: |      |                    |
| `/v1/swift-codes/{swift-code}`              | Delete bank info for given SWIFT code           |                    |                    |      | :heavy_check_mark: |

# Database

For database Used PostgreSQL

### How to install locally ?

* Download zip with binaries and extract on your computer
  https://www.enterprisedb.com/download-postgresql-binaries;
* How to initialize local database

``` 
  initdb.exe -D ../data –-username=admin –auth=trust
```

* How to start local DB server

```
pg_ctl start -D../data
```

* You may use e.g. DBWeaver in order to connect into database from outside of the service.
  Use https://dbeaver.io/download/