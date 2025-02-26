CREATE TABLE IF NOT EXISTS problems (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    subcategory VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    file_id TEXT,
    status VARCHAR(50) NOT NULL,
    specialist_id BIGINT,
    client_chat_id BIGINT NOT NULL,

    FOREIGN KEY (client_chat_id) REFERENCES clients(chat_id),
    FOREIGN KEY (specialist_id) REFERENCES specialists(id)
)