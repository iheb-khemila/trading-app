CREATE TABLE investor (
                          investor_id INT AUTO_INCREMENT PRIMARY KEY,
                          username VARCHAR(20) NOT NULL UNIQUE,
                          firstname VARCHAR(20) NOT NULL,
                          lastname VARCHAR(20) NOT NULL,
                          password_hash BINARY(128) NOT NULL,
                          password_salt BINARY(32) NOT NULL
) CHARACTER SET utf8mb4;

CREATE TABLE broker (
                        broker_id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(20) NOT NULL UNIQUE,
                        company VARCHAR(20) NOT NULL,
                        password_hash BINARY(128) NOT NULL,
                        password_salt BINARY(32) NOT NULL
) CHARACTER SET utf8mb4;


CREATE TABLE portfolio (
                           portfolio_id INT AUTO_INCREMENT PRIMARY KEY,
                           investor_id INT NOT NULL,
                           broker_id INT NOT NULL,
                           creation_date TIMESTAMP NOT NULL,
                           FOREIGN KEY (investor_id) REFERENCES investor(investor_id),
                           FOREIGN KEY (broker_id) REFERENCES broker(broker_id),
                           CONSTRAINT investorRelation UNIQUE (investor_id,broker_id)
) CHARACTER SET utf8mb4;

CREATE TABLE asset (
                       asset_id INT AUTO_INCREMENT PRIMARY KEY,
                       broker_id INT NOT NULL,
                       kind ENUM ('Share', 'Bond') NOT NULL,
                       name VARCHAR(20) NOT NULL,
                       FOREIGN KEY (broker_id) REFERENCES broker(broker_id)
) CHARACTER SET utf8mb4;

CREATE TABLE portfolio_assets (
                                  portfolio_id INT NOT NULL,
                                  asset_id INT NOT NULL,
                                  PRIMARY KEY (portfolio_id, asset_id),
                                  FOREIGN KEY (portfolio_id) REFERENCES portfolio(portfolio_id),
                                  FOREIGN KEY (asset_id) REFERENCES asset(asset_id)
) CHARACTER SET utf8mb4;