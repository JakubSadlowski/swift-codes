CREATE TABLE IF NOT EXISTS swift_codes (
    id INTEGER PRIMARY KEY,
    swift_code VARCHAR(11) NOT NULL UNIQUE,
    country_iso2_code CHAR(2) NOT NULL,
    is_headquarter BOOLEAN NOT null,
    name VARCHAR(512) NOT NULL,
    code_type VARCHAR(5) NOT NULL,
    address VARCHAR(512),
    town_name VARCHAR(255) NOT NULL,
    country_name VARCHAR(255) NOT NULL,
    time_zone VARCHAR(100) NOT NULL,
    parent_id INTEGER,
    FOREIGN KEY (parent_id) REFERENCES swift_codes(id)
);

CREATE INDEX swift_codes_parent_id_key ON swift_codes (parent_id);