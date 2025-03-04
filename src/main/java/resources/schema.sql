-- Drop tables if they exist
DROP TABLE IF EXISTS securities;

-- Create securities table
CREATE TABLE securities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticker VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(10) NOT NULL, -- STOCK, CALL, PUT
    strike DOUBLE DEFAULT NULL,
    maturity DATE DEFAULT NULL,
    underlying_ticker VARCHAR(50) DEFAULT NULL,
    expected_return DOUBLE DEFAULT NULL,
    volatility DOUBLE DEFAULT NULL
);

-- Insert some sample data for stocks
INSERT INTO securities (ticker, type, expected_return, volatility) VALUES
('AAPL', 'STOCK', 0.2, 0.3),
('TELSA', 'STOCK', 0.5, 0.8);

-- Insert some sample data for options
INSERT INTO securities (ticker, type, strike, maturity, underlying_ticker, expected_return, volatility) VALUES
('AAPL-OCT-2020-110-C', 'CALL', 110.0, '2020-10-15', 'AAPL', NULL, NULL),
('AAPL-OCT-2020-110-P', 'PUT', 110.0, '2020-10-15', 'AAPL', NULL, NULL),
('TELSA-NOV-2020-400-C', 'CALL', 400.0, '2020-11-15', 'TELSA', NULL, NULL),
('TELSA-DEC-2020-400-P', 'PUT', 400.0, '2020-12-15', 'TELSA', NULL, NULL);