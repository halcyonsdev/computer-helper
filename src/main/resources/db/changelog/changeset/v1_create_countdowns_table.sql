CREATE TABLE IF NOT EXISTS countdowns (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    file_id TEXT,
    problem_id BIGINT NOT NULL,

    FOREIGN KEY (problem_id) REFERENCES problems(id)
)