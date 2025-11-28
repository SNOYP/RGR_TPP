-- Паролі закодовані в Base64: "admin" -> YWRtaW4=, "user" -> dXNlcg==
INSERT INTO users (username, password, role)
VALUES ('admin', 'YWRtaW4=', 'ROLE_ADMIN')
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, role)
VALUES ('user', 'dXNlcg==', 'ROLE_USER')
ON CONFLICT (username) DO NOTHING;