INSERT INTO users (first_name, last_name, age) VALUES 
('Ion', 'Popescu', 25),
('Maria', 'Ionescu', 22),
('Andrei', 'Vasile', 30),
('Elena', 'Gheorghe', 28);

INSERT INTO friendships (id1, id2, friends_from) VALUES 
(1, 2, CURRENT_TIMESTAMP),
(1, 3, '2023-11-15 14:30:00'),
(2, 4, CURRENT_TIMESTAMP);

SELECT * FROM users;
SELECT * FROM friendships;