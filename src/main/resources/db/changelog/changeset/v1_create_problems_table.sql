CREATE TABLE IF NOT EXISTS problems (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    subcategory VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    image_url TEXT,
    status VARCHAR(10) NOT NULL,
    specialist_id BIGINT,
    client_id BIGINT NOT NULL,

    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (specialist_id) REFERENCES specialists(id)
)