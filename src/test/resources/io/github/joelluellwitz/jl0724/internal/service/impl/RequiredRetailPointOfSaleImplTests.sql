INSERT INTO tool_type
  (id, version, created_on, updated_on, name, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES
  (1, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Ladder', '1.99', true, true, false),
  (2, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Chainsaw', '1.49', true, false, true),
  (3, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'Jackhammer', '2.99', true, false, false);

INSERT INTO tool
  (version, created_on, updated_on, code, tool_type_id, brand)
VALUES
  (0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'CHNS', 2, 'Stihl'),
  (0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'LADW', 1, 'Werner'),
  (0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'JAKD', 3, 'DeWalt'),
  (0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'JAKR', 3, 'Ridgid');