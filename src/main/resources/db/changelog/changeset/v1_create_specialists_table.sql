CREATE TABLE IF NOT EXISTS specialists(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    is_request BOOLEAN NOT NULL
)