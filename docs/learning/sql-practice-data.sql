-- ============================================================
--  SQL Practice Dataset — Banking Theme
--  Tables: branches, customers, accounts, transactions
--  Designed for learning: window functions, CTEs, aggregations,
--  JOINs, subqueries, HAVING, CASE, and more.
--
--  Run the entire file in pgAdmin Query Tool (F5 / Execute)
-- ============================================================

-- ─────────────────────────────────────────────────────────────
--  CLEANUP (safe to re-run)
-- ─────────────────────────────────────────────────────────────
DROP TABLE IF EXISTS transactions  CASCADE;
DROP TABLE IF EXISTS accounts      CASCADE;
DROP TABLE IF EXISTS customers     CASCADE;
DROP TABLE IF EXISTS branches      CASCADE;

-- ─────────────────────────────────────────────────────────────
--  TABLE 1 : branches
-- ─────────────────────────────────────────────────────────────
CREATE TABLE branches (
    branch_id   SERIAL       PRIMARY KEY,
    branch_name VARCHAR(60)  NOT NULL,
    city        VARCHAR(40)  NOT NULL,
    state       VARCHAR(30)  NOT NULL,
    opened_on   DATE         NOT NULL
);

INSERT INTO branches (branch_name, city, state, opened_on) VALUES
    ('Downtown Central',    'New York',     'NY', '2005-03-15'),
    ('Midtown Plaza',       'New York',     'NY', '2008-07-22'),
    ('Westside Branch',     'Los Angeles',  'CA', '2006-11-01'),
    ('Silicon Valley Hub',  'San Jose',     'CA', '2010-05-30'),
    ('Lakeshore Office',    'Chicago',      'IL', '2007-09-10'),
    ('River North',         'Chicago',      'IL', '2012-02-14'),
    ('Peachtree Center',    'Atlanta',      'GA', '2009-08-03'),
    ('Lone Star Branch',    'Dallas',       'TX', '2011-04-20'),
    ('Space City Office',   'Houston',      'TX', '2013-06-17'),
    ('Pacific Heights',     'Seattle',      'WA', '2015-01-28');

-- ─────────────────────────────────────────────────────────────
--  TABLE 2 : customers
-- ─────────────────────────────────────────────────────────────
CREATE TABLE customers (
    customer_id   SERIAL       PRIMARY KEY,
    first_name    VARCHAR(40)  NOT NULL,
    last_name     VARCHAR(40)  NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    phone         VARCHAR(20),
    date_of_birth DATE         NOT NULL,
    gender        CHAR(1)      CHECK (gender IN ('M','F','O')),
    signup_date   DATE         NOT NULL,
    branch_id     INT          REFERENCES branches(branch_id),
    segment       VARCHAR(20)  CHECK (segment IN ('Retail','Premium','Business'))
);

INSERT INTO customers
    (first_name, last_name, email, phone, date_of_birth, gender, signup_date, branch_id, segment)
VALUES
    ('Alice',     'Morgan',     'alice.morgan@email.com',     '212-555-0101', '1985-04-12', 'F', '2015-06-01', 1, 'Premium'),
    ('Bob',       'Chen',       'bob.chen@email.com',         '212-555-0102', '1990-08-23', 'M', '2016-02-15', 1, 'Retail'),
    ('Carol',     'Davis',      'carol.davis@email.com',      '213-555-0103', '1978-12-05', 'F', '2014-09-20', 3, 'Business'),
    ('David',     'Kim',        'david.kim@email.com',        '408-555-0104', '1993-03-17', 'M', '2017-11-08', 4, 'Retail'),
    ('Eva',       'Patel',      'eva.patel@email.com',        '312-555-0105', '1988-07-30', 'F', '2016-05-22', 5, 'Premium'),
    ('Frank',     'Garcia',     'frank.garcia@email.com',     '312-555-0106', '1975-01-14', 'M', '2013-03-10', 6, 'Business'),
    ('Grace',     'Wilson',     'grace.wilson@email.com',     '404-555-0107', '1995-09-02', 'F', '2019-07-18', 7, 'Retail'),
    ('Henry',     'Martinez',   'henry.martinez@email.com',   '214-555-0108', '1982-11-25', 'M', '2015-12-05', 8, 'Premium'),
    ('Isabelle',  'Thompson',   'isabelle.t@email.com',       '713-555-0109', '1991-06-08', 'F', '2018-04-30', 9, 'Retail'),
    ('James',     'Anderson',   'james.anderson@email.com',   '206-555-0110', '1987-02-19', 'M', '2017-08-14', 10,'Premium'),
    ('Karen',     'White',      'karen.white@email.com',      '212-555-0111', '1980-05-27', 'F', '2014-01-09', 2, 'Business'),
    ('Leo',       'Harris',     'leo.harris@email.com',       '213-555-0112', '1998-10-15', 'M', '2020-03-22', 3, 'Retail'),
    ('Mia',       'Jackson',    'mia.jackson@email.com',      '408-555-0113', '1994-07-04', 'F', '2019-11-11', 4, 'Retail'),
    ('Nathan',    'Lee',        'nathan.lee@email.com',       '312-555-0114', '1986-03-21', 'M', '2016-09-03', 5, 'Premium'),
    ('Olivia',    'Taylor',     'olivia.taylor@email.com',    '404-555-0115', '1992-12-30', 'F', '2018-06-25', 7, 'Retail'),
    ('Patrick',   'Brown',      'patrick.brown@email.com',    '214-555-0116', '1979-08-11', 'M', '2013-07-16', 8, 'Business'),
    ('Quinn',     'Miller',     'quinn.miller@email.com',     '713-555-0117', '1996-04-18', 'F', '2021-01-07', 9, 'Retail'),
    ('Ryan',      'Wilson',     'ryan.wilson@email.com',      '206-555-0118', '1983-09-29', 'M', '2015-10-20', 10,'Premium'),
    ('Sophia',    'Moore',      'sophia.moore@email.com',     '212-555-0119', '1997-01-06', 'F', '2021-08-15', 1, 'Retail'),
    ('Tyler',     'Johnson',    'tyler.johnson@email.com',    '312-555-0120', '1989-06-13', 'M', '2017-04-01', 6, 'Business'),
    ('Uma',       'Clark',      'uma.clark@email.com',        '213-555-0121', '1984-11-22', 'F', '2016-07-30', 3, 'Premium'),
    ('Victor',    'Lewis',      'victor.lewis@email.com',     '408-555-0122', '1990-03-08', 'M', '2018-02-14', 4, 'Business'),
    ('Wendy',     'Robinson',   'wendy.robinson@email.com',   '404-555-0123', '1977-07-19', 'F', '2012-11-05', 7, 'Business'),
    ('Xander',    'Walker',     'xander.walker@email.com',    '214-555-0124', '1999-02-28', 'M', '2022-06-10', 8, 'Retail'),
    ('Yara',      'Hall',       'yara.hall@email.com',        '206-555-0125', '1993-10-01', 'F', '2019-09-27', 10,'Premium');

-- ─────────────────────────────────────────────────────────────
--  TABLE 3 : accounts
-- ─────────────────────────────────────────────────────────────
CREATE TABLE accounts (
    account_id     SERIAL        PRIMARY KEY,
    customer_id    INT           NOT NULL REFERENCES customers(customer_id),
    account_type   VARCHAR(20)   NOT NULL CHECK (account_type IN ('Checking','Savings','Credit','Investment')),
    account_number VARCHAR(14)   NOT NULL UNIQUE,
    balance        NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    opened_date    DATE          NOT NULL,
    status         VARCHAR(10)   NOT NULL DEFAULT 'Active' CHECK (status IN ('Active','Inactive','Closed')),
    interest_rate  NUMERIC(5,3)
);

INSERT INTO accounts
    (customer_id, account_type, account_number, balance, opened_date, status, interest_rate)
VALUES
    -- Alice
    (1,  'Checking',   'CHK-0001-0001',  12450.75, '2015-06-01', 'Active',   0.010),
    (1,  'Savings',    'SAV-0001-0002',  87320.50, '2015-06-01', 'Active',   3.500),
    -- Bob
    (2,  'Checking',   'CHK-0002-0001',   3200.00, '2016-02-15', 'Active',   0.010),
    -- Carol
    (3,  'Checking',   'CHK-0003-0001',  45600.00, '2014-09-20', 'Active',   0.010),
    (3,  'Investment', 'INV-0003-0001', 210000.00, '2015-01-10', 'Active',   5.250),
    -- David
    (4,  'Checking',   'CHK-0004-0001',   1800.25, '2017-11-08', 'Active',   0.010),
    (4,  'Savings',    'SAV-0004-0001',   9400.00, '2017-11-08', 'Active',   3.500),
    -- Eva
    (5,  'Savings',    'SAV-0005-0001',  56000.00, '2016-05-22', 'Active',   3.750),
    (5,  'Investment', 'INV-0005-0001', 145000.00, '2017-03-01', 'Active',   4.900),
    -- Frank
    (6,  'Checking',   'CHK-0006-0001',  23000.00, '2013-03-10', 'Active',   0.010),
    (6,  'Credit',     'CRD-0006-0001',  -4200.00, '2014-06-01', 'Active',  18.990),
    -- Grace
    (7,  'Checking',   'CHK-0007-0001',    850.50, '2019-07-18', 'Active',   0.010),
    -- Henry
    (8,  'Checking',   'CHK-0008-0001',  31500.00, '2015-12-05', 'Active',   0.010),
    (8,  'Savings',    'SAV-0008-0001',  72000.00, '2015-12-05', 'Active',   3.500),
    -- Isabelle
    (9,  'Checking',   'CHK-0009-0001',   4100.75, '2018-04-30', 'Active',   0.010),
    (9,  'Credit',     'CRD-0009-0001',  -1500.00, '2019-01-15', 'Active',  21.990),
    -- James
    (10, 'Savings',    'SAV-0010-0001',  98750.00, '2017-08-14', 'Active',   4.000),
    (10, 'Investment', 'INV-0010-0001', 320000.00, '2018-01-01', 'Active',   6.100),
    -- Karen
    (11, 'Checking',   'CHK-0011-0001',  18200.00, '2014-01-09', 'Active',   0.010),
    (11, 'Credit',     'CRD-0011-0001',  -8900.00, '2015-04-20', 'Active',  16.990),
    -- Leo
    (12, 'Checking',   'CHK-0012-0001',   2200.00, '2020-03-22', 'Active',   0.010),
    -- Mia
    (13, 'Checking',   'CHK-0013-0001',   5600.00, '2019-11-11', 'Active',   0.010),
    (13, 'Savings',    'SAV-0013-0001',  14300.00, '2019-11-11', 'Active',   3.500),
    -- Nathan
    (14, 'Investment', 'INV-0014-0001', 175000.00, '2016-09-03', 'Active',   5.000),
    (14, 'Savings',    'SAV-0014-0001',  44000.00, '2016-09-03', 'Active',   3.750),
    -- Olivia
    (15, 'Checking',   'CHK-0015-0001',   7300.25, '2018-06-25', 'Active',   0.010),
    -- Patrick
    (16, 'Checking',   'CHK-0016-0001',  62000.00, '2013-07-16', 'Active',   0.010),
    (16, 'Investment', 'INV-0016-0001', 480000.00, '2014-02-01', 'Active',   5.750),
    -- Quinn
    (17, 'Checking',   'CHK-0017-0001',   1150.00, '2021-01-07', 'Active',   0.010),
    -- Ryan
    (18, 'Savings',    'SAV-0018-0001',  63000.00, '2015-10-20', 'Active',   3.500),
    (18, 'Investment', 'INV-0018-0001', 290000.00, '2016-04-15', 'Active',   5.500),
    -- Sophia
    (19, 'Checking',   'CHK-0019-0001',   3700.00, '2021-08-15', 'Active',   0.010),
    -- Tyler
    (20, 'Checking',   'CHK-0020-0001',  29500.00, '2017-04-01', 'Active',   0.010),
    (20, 'Credit',     'CRD-0020-0001', -11200.00, '2018-05-10', 'Active',  19.990),
    -- Uma
    (21, 'Savings',    'SAV-0021-0001',  52000.00, '2016-07-30', 'Active',   3.750),
    (21, 'Investment', 'INV-0021-0001', 130000.00, '2017-05-01', 'Active',   4.750),
    -- Victor
    (22, 'Checking',   'CHK-0022-0001',  37000.00, '2018-02-14', 'Active',   0.010),
    (22, 'Credit',     'CRD-0022-0001',  -6700.00, '2019-03-01', 'Active',  17.990),
    -- Wendy
    (23, 'Checking',   'CHK-0023-0001',  95000.00, '2012-11-05', 'Active',   0.010),
    (23, 'Investment', 'INV-0023-0001', 560000.00, '2013-06-01', 'Active',   6.000),
    -- Xander
    (24, 'Checking',   'CHK-0024-0001',    400.00, '2022-06-10', 'Active',   0.010),
    -- Yara
    (25, 'Savings',    'SAV-0025-0001',  41500.00, '2019-09-27', 'Active',   3.500),
    (25, 'Investment', 'INV-0025-0001', 185000.00, '2020-04-01', 'Active',   5.100);

-- ─────────────────────────────────────────────────────────────
--  TABLE 4 : transactions
-- ─────────────────────────────────────────────────────────────
CREATE TABLE transactions (
    txn_id         SERIAL        PRIMARY KEY,
    account_id     INT           NOT NULL REFERENCES accounts(account_id),
    txn_type       VARCHAR(20)   NOT NULL CHECK (txn_type IN ('Deposit','Withdrawal','Transfer','Payment','Fee','Interest')),
    amount         NUMERIC(15,2) NOT NULL,
    txn_date       DATE          NOT NULL,
    description    VARCHAR(200),
    channel        VARCHAR(20)   CHECK (channel IN ('Online','ATM','Branch','Mobile','Auto'))
);

INSERT INTO transactions
    (account_id, txn_type, amount, txn_date, description, channel)
VALUES
-- ── Account 1 (Alice - Checking) ──
    (1, 'Deposit',    3500.00, '2024-01-05', 'Salary deposit',           'Auto'),
    (1, 'Withdrawal',  200.00, '2024-01-08', 'ATM cash withdrawal',      'ATM'),
    (1, 'Payment',     150.00, '2024-01-12', 'Electric bill payment',    'Online'),
    (1, 'Deposit',    3500.00, '2024-02-05', 'Salary deposit',           'Auto'),
    (1, 'Transfer',   1000.00, '2024-02-10', 'Transfer to savings',      'Mobile'),
    (1, 'Payment',      85.00, '2024-02-15', 'Internet bill',            'Online'),
    (1, 'Deposit',    3500.00, '2024-03-05', 'Salary deposit',           'Auto'),
    (1, 'Withdrawal',  300.00, '2024-03-18', 'Cash withdrawal',          'ATM'),
    (1, 'Fee',          15.00, '2024-03-31', 'Monthly service fee',      'Auto'),
    (1, 'Deposit',    3500.00, '2024-04-05', 'Salary deposit',           'Auto'),

-- ── Account 2 (Alice - Savings) ──
    (2, 'Deposit',    5000.00, '2024-01-15', 'Initial top-up',           'Branch'),
    (2, 'Interest',    254.00, '2024-01-31', 'Monthly interest credit',  'Auto'),
    (2, 'Deposit',    1000.00, '2024-02-10', 'Transfer from checking',   'Mobile'),
    (2, 'Interest',    258.00, '2024-02-29', 'Monthly interest credit',  'Auto'),
    (2, 'Interest',    261.00, '2024-03-31', 'Monthly interest credit',  'Auto'),

-- ── Account 3 (Bob - Checking) ──
    (3, 'Deposit',    2500.00, '2024-01-03', 'Salary deposit',           'Auto'),
    (3, 'Withdrawal',  400.00, '2024-01-10', 'ATM withdrawal',           'ATM'),
    (3, 'Payment',     120.00, '2024-01-20', 'Phone bill',               'Mobile'),
    (3, 'Deposit',    2500.00, '2024-02-03', 'Salary deposit',           'Auto'),
    (3, 'Payment',      60.00, '2024-02-22', 'Streaming subscription',   'Online'),
    (3, 'Deposit',    2500.00, '2024-03-03', 'Salary deposit',           'Auto'),
    (3, 'Fee',          12.00, '2024-03-31', 'Monthly service fee',      'Auto'),

-- ── Account 4 (Carol - Checking) ──
    (4, 'Deposit',   15000.00, '2024-01-02', 'Business revenue',         'Branch'),
    (4, 'Payment',    5000.00, '2024-01-15', 'Vendor payment',           'Online'),
    (4, 'Deposit',   18000.00, '2024-02-02', 'Business revenue',         'Branch'),
    (4, 'Payment',    6500.00, '2024-02-20', 'Payroll disbursement',     'Online'),
    (4, 'Deposit',   21000.00, '2024-03-02', 'Business revenue',         'Branch'),
    (4, 'Payment',    4800.00, '2024-03-18', 'Vendor payment',           'Online'),

-- ── Account 6 (David - Checking) ──
    (6, 'Deposit',    1800.00, '2024-01-04', 'Salary deposit',           'Auto'),
    (6, 'Withdrawal',  250.00, '2024-01-14', 'ATM withdrawal',           'ATM'),
    (6, 'Payment',     110.00, '2024-02-05', 'Grocery store',            'Mobile'),
    (6, 'Deposit',    1800.00, '2024-02-04', 'Salary deposit',           'Auto'),
    (6, 'Deposit',    1800.00, '2024-03-04', 'Salary deposit',           'Auto'),
    (6, 'Fee',          10.00, '2024-03-31', 'Monthly service fee',      'Auto'),

-- ── Account 8 (Eva - Savings) ──
    (8, 'Deposit',    2000.00, '2024-01-06', 'Monthly saving',           'Online'),
    (8, 'Interest',   175.00,  '2024-01-31', 'Monthly interest',         'Auto'),
    (8, 'Deposit',    2000.00, '2024-02-06', 'Monthly saving',           'Online'),
    (8, 'Interest',   177.00,  '2024-02-29', 'Monthly interest',         'Auto'),
    (8, 'Deposit',    2000.00, '2024-03-06', 'Monthly saving',           'Online'),
    (8, 'Interest',   179.00,  '2024-03-31', 'Monthly interest',         'Auto'),

-- ── Account 10 (Frank - Checking) ──
    (10,'Deposit',   10000.00, '2024-01-02', 'Business deposit',         'Branch'),
    (10,'Payment',    3200.00, '2024-01-17', 'Office rent payment',      'Online'),
    (10,'Deposit',   12000.00, '2024-02-02', 'Business deposit',         'Branch'),
    (10,'Payment',    3200.00, '2024-02-17', 'Office rent payment',      'Online'),
    (10,'Deposit',    9500.00, '2024-03-02', 'Business deposit',         'Branch'),
    (10,'Payment',    3200.00, '2024-03-17', 'Office rent payment',      'Online'),

-- ── Account 11 (Frank - Credit) ──
    (11,'Payment',     500.00, '2024-01-10', 'Credit card payment',      'Online'),
    (11,'Fee',          75.00, '2024-01-31', 'Monthly interest charge',  'Auto'),
    (11,'Payment',     500.00, '2024-02-10', 'Credit card payment',      'Online'),
    (11,'Fee',          70.00, '2024-02-29', 'Monthly interest charge',  'Auto'),
    (11,'Payment',     800.00, '2024-03-10', 'Credit card payment',      'Online'),
    (11,'Fee',          65.00, '2024-03-31', 'Monthly interest charge',  'Auto'),

-- ── Account 13 (Henry - Checking) ──
    (13,'Deposit',    8000.00, '2024-01-03', 'Salary deposit',           'Auto'),
    (13,'Withdrawal', 1500.00, '2024-01-12', 'ATM withdrawal',           'ATM'),
    (13,'Payment',     420.00, '2024-01-25', 'Utilities payment',        'Online'),
    (13,'Deposit',    8000.00, '2024-02-03', 'Salary deposit',           'Auto'),
    (13,'Withdrawal',  600.00, '2024-02-16', 'ATM withdrawal',           'ATM'),
    (13,'Deposit',    8000.00, '2024-03-03', 'Salary deposit',           'Auto'),
    (13,'Payment',     380.00, '2024-03-22', 'Insurance premium',        'Online'),

-- ── Account 16 (James - Savings) ──
    (16,'Deposit',    5000.00, '2024-01-08', 'Monthly contribution',     'Mobile'),
    (16,'Interest',   329.00,  '2024-01-31', 'Monthly interest',         'Auto'),
    (16,'Deposit',    5000.00, '2024-02-08', 'Monthly contribution',     'Mobile'),
    (16,'Interest',   333.00,  '2024-02-29', 'Monthly interest',         'Auto'),
    (16,'Deposit',    5000.00, '2024-03-08', 'Monthly contribution',     'Mobile'),
    (16,'Interest',   338.00,  '2024-03-31', 'Monthly interest',         'Auto'),

-- ── Account 19 (Karen - Checking) ──
    (19,'Deposit',    6000.00, '2024-01-02', 'Business income',          'Branch'),
    (19,'Payment',    2100.00, '2024-01-14', 'Equipment purchase',       'Online'),
    (19,'Deposit',    7500.00, '2024-02-02', 'Business income',          'Branch'),
    (19,'Payment',    1800.00, '2024-02-20', 'Supplier payment',         'Online'),
    (19,'Deposit',    6800.00, '2024-03-02', 'Business income',          'Branch'),
    (19,'Fee',          20.00, '2024-03-31', 'Business account fee',     'Auto'),

-- ── Account 22 (Leo - Checking) ──
    (22,'Deposit',    1500.00, '2024-01-06', 'Part-time income',         'Mobile'),
    (22,'Withdrawal',  200.00, '2024-01-20', 'Cash withdrawal',          'ATM'),
    (22,'Deposit',    1500.00, '2024-02-06', 'Part-time income',         'Mobile'),
    (22,'Payment',      55.00, '2024-02-25', 'Streaming service',        'Online'),
    (22,'Deposit',    1500.00, '2024-03-06', 'Part-time income',         'Mobile'),
    (22,'Fee',           8.00, '2024-03-31', 'Monthly fee',              'Auto'),

-- ── Account 27 (Patrick - Checking) ──
    (27,'Deposit',   20000.00, '2024-01-02', 'Business revenue',         'Branch'),
    (27,'Payment',    7500.00, '2024-01-10', 'Payroll',                  'Online'),
    (27,'Payment',    4200.00, '2024-01-22', 'Vendor A payment',         'Online'),
    (27,'Deposit',   22000.00, '2024-02-02', 'Business revenue',         'Branch'),
    (27,'Payment',    7500.00, '2024-02-10', 'Payroll',                  'Online'),
    (27,'Payment',    3800.00, '2024-02-24', 'Vendor B payment',         'Online'),
    (27,'Deposit',   19500.00, '2024-03-02', 'Business revenue',         'Branch'),
    (27,'Payment',    7500.00, '2024-03-10', 'Payroll',                  'Online'),

-- ── Account 33 (Sophia - Checking) ──
    (33,'Deposit',    2200.00, '2024-01-05', 'Freelance payment',        'Mobile'),
    (33,'Withdrawal',  350.00, '2024-01-19', 'Cash withdrawal',          'ATM'),
    (33,'Deposit',    1800.00, '2024-02-07', 'Freelance payment',        'Mobile'),
    (33,'Payment',     100.00, '2024-02-28', 'Gym membership',           'Online'),
    (33,'Deposit',    2500.00, '2024-03-05', 'Freelance payment',        'Mobile'),
    (33,'Fee',          10.00, '2024-03-31', 'Monthly service fee',      'Auto'),

-- ── Account 40 (Wendy - Checking) ──
    (40,'Deposit',   30000.00, '2024-01-02', 'Business revenue',         'Branch'),
    (40,'Payment',    8000.00, '2024-01-08', 'Payroll',                  'Online'),
    (40,'Payment',    6500.00, '2024-01-18', 'Office lease',             'Online'),
    (40,'Deposit',   35000.00, '2024-02-02', 'Business revenue',         'Branch'),
    (40,'Payment',    8000.00, '2024-02-08', 'Payroll',                  'Online'),
    (40,'Payment',    6500.00, '2024-02-18', 'Office lease',             'Online'),
    (40,'Deposit',   28000.00, '2024-03-02', 'Business revenue',         'Branch'),
    (40,'Payment',    8000.00, '2024-03-08', 'Payroll',                  'Online'),
    (40,'Payment',    6500.00, '2024-03-18', 'Office lease',             'Online'),

-- ── Account 42 (Xander - Checking) ──
    (42,'Deposit',     500.00, '2024-01-10', 'Gift deposit',             'Branch'),
    (42,'Withdrawal',  100.00, '2024-02-14', 'Cash withdrawal',          'ATM'),
    (42,'Deposit',     300.00, '2024-03-01', 'Odd-job income',           'Mobile'),

-- ── Account 43 (Yara - Savings) ──
    (43,'Deposit',    3000.00, '2024-01-09', 'Monthly saving',           'Online'),
    (43,'Interest',   121.00,  '2024-01-31', 'Monthly interest',         'Auto'),
    (43,'Deposit',    3000.00, '2024-02-09', 'Monthly saving',           'Online'),
    (43,'Interest',   122.00,  '2024-02-29', 'Monthly interest',         'Auto'),
    (43,'Deposit',    3000.00, '2024-03-09', 'Monthly saving',           'Online'),
    (43,'Interest',   124.00,  '2024-03-31', 'Monthly interest',         'Auto');

-- ─────────────────────────────────────────────────────────────
--  VERIFICATION — quick row counts
-- ─────────────────────────────────────────────────────────────
SELECT 'branches'     AS tbl, COUNT(*) FROM branches
UNION ALL
SELECT 'customers',          COUNT(*) FROM customers
UNION ALL
SELECT 'accounts',           COUNT(*) FROM accounts
UNION ALL
SELECT 'transactions',       COUNT(*) FROM transactions;

-- ============================================================
--  SAMPLE QUERIES TO EXPLORE
-- ============================================================

-- ── 1. ROW_NUMBER — rank accounts per customer by balance ──
/*
SELECT
    c.first_name || ' ' || c.last_name        AS customer,
    a.account_type,
    a.balance,
    ROW_NUMBER() OVER (
        PARTITION BY a.customer_id
        ORDER BY a.balance DESC
    )                                          AS rn_by_balance
FROM accounts a
JOIN customers c USING (customer_id)
ORDER BY customer, rn_by_balance;
*/

-- ── 2. RANK / DENSE_RANK — rank customers by total balance ──
/*
SELECT
    c.first_name || ' ' || c.last_name   AS customer,
    SUM(a.balance)                        AS total_balance,
    RANK()       OVER (ORDER BY SUM(a.balance) DESC) AS rnk,
    DENSE_RANK() OVER (ORDER BY SUM(a.balance) DESC) AS dense_rnk
FROM customers c
JOIN accounts a USING (customer_id)
GROUP BY c.customer_id, c.first_name, c.last_name
ORDER BY rnk;
*/

-- ── 3. SUM OVER — running total of transactions per account ──
/*
SELECT
    account_id,
    txn_date,
    txn_type,
    amount,
    SUM(CASE WHEN txn_type IN ('Deposit','Interest') THEN amount
             ELSE -amount END)
        OVER (PARTITION BY account_id ORDER BY txn_date, txn_id)
                                          AS running_balance
FROM transactions
ORDER BY account_id, txn_date, txn_id;
*/

-- ── 4. LAG / LEAD — month-over-month deposit change ──
/*
WITH monthly AS (
    SELECT
        DATE_TRUNC('month', txn_date)::DATE  AS month,
        SUM(amount)                           AS total_deposits
    FROM transactions
    WHERE txn_type = 'Deposit'
    GROUP BY 1
)
SELECT
    month,
    total_deposits,
    LAG(total_deposits)  OVER (ORDER BY month) AS prev_month,
    LEAD(total_deposits) OVER (ORDER BY month) AS next_month,
    total_deposits - LAG(total_deposits) OVER (ORDER BY month) AS mom_change
FROM monthly
ORDER BY month;
*/

-- ── 5. NTILE — bucket customers by balance into quartiles ──
/*
SELECT
    c.first_name || ' ' || c.last_name  AS customer,
    SUM(a.balance)                       AS total_balance,
    NTILE(4) OVER (ORDER BY SUM(a.balance)) AS quartile
FROM customers c
JOIN accounts a USING (customer_id)
GROUP BY c.customer_id, c.first_name, c.last_name
ORDER BY total_balance;
*/

-- ── 6. PERCENT_RANK & CUME_DIST ──
/*
SELECT
    c.first_name || ' ' || c.last_name        AS customer,
    c.segment,
    SUM(a.balance)                             AS total_balance,
    ROUND(PERCENT_RANK() OVER (
        PARTITION BY c.segment
        ORDER BY SUM(a.balance))::NUMERIC, 4)  AS pct_rank_in_segment,
    ROUND(CUME_DIST()    OVER (
        PARTITION BY c.segment
        ORDER BY SUM(a.balance))::NUMERIC, 4)  AS cume_dist_in_segment
FROM customers c
JOIN accounts a USING (customer_id)
GROUP BY c.customer_id, c.first_name, c.last_name, c.segment
ORDER BY c.segment, total_balance;
*/

-- ── 7. FIRST_VALUE / LAST_VALUE — top earner per branch ──
/*
SELECT DISTINCT
    b.branch_name,
    FIRST_VALUE(c.first_name || ' ' || c.last_name)
        OVER (PARTITION BY b.branch_id
              ORDER BY SUM(a.balance) DESC
              ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)
                                          AS top_customer,
    LAST_VALUE(c.first_name || ' ' || c.last_name)
        OVER (PARTITION BY b.branch_id
              ORDER BY SUM(a.balance) DESC
              ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)
                                          AS lowest_customer
FROM branches b
JOIN customers c USING (branch_id)
JOIN accounts  a USING (customer_id)
GROUP BY b.branch_id, b.branch_name, c.customer_id, c.first_name, c.last_name;
*/

-- ── 8. CTE + window — customers above their segment avg balance ──
/*
WITH seg_avg AS (
    SELECT
        c.customer_id,
        c.first_name || ' ' || c.last_name AS customer,
        c.segment,
        SUM(a.balance)                      AS total_balance,
        AVG(SUM(a.balance)) OVER (PARTITION BY c.segment) AS segment_avg
    FROM customers c
    JOIN accounts a USING (customer_id)
    GROUP BY c.customer_id, c.first_name, c.last_name, c.segment
)
SELECT *
FROM seg_avg
WHERE total_balance > segment_avg
ORDER BY segment, total_balance DESC;
*/
