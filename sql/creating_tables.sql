CREATE TABLE users (
    id TEXT,
    email TEXT
);

CREATE TABLE surveys (
    id TEXT,
    user_id TEXT,
    survey TEXT
);

CREATE TABLE responses (
    id TEXT,
    survey_id TEXT,
    user_id TEXT,
    response TEXT
);