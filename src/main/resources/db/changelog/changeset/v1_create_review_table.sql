CREATE TABLE IF NOT EXISTS reviews (
    id BIGSERIAL PRIMARY KEY,
    starts_count INT NOT NULL,
    content TEXT,
    specialist_chat_id BIGINT NOT NULL,
    client_chat_id BIGINT NOT NULL,

    FOREIGN KEY (specialist_chat_id) REFERENCES specialists(chat_id),
    FOREIGN KEY (client_chat_id) REFERENCES clients(chat_id)
)