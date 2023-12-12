-- Tworzenie tabeli STATUS
CREATE TABLE STATUS (
    status_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL
);

-- Tworzenie tabeli COLLECTION
CREATE TABLE COLLECTION (
    collection_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(100) NOT NULL,
    start_date DATE,
    finish_date DATE,
    description TEXT
);

-- Tworzenie tabeli COLLECTIBLE
CREATE TABLE COLLECTIBLE (
    collectible_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(100) NOT NULL,
    start_date DATE,
    finish_date DATE,
    description TEXT,
    status_id INTEGER,
    collection_id INTEGER NOT NULL,
    FOREIGN KEY (collection_id) REFERENCES COLLECTION(collection_id)
);

-- Tworzenie tabeli SEGMENT
CREATE TABLE SEGMENT (
    segment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title VARCHAR(100) NOT NULL,
    start_datetime DATETIME,
    finish_datetime DATETIME,
    description TEXT,
    coords TEXT,
    status_id INTEGER,
    collectible_id INTEGER NOT NULL,
    thumbnail_id INTEGER NOT NULL,
    FOREIGN KEY (status_id) REFERENCES STATUS(status_id),
    FOREIGN KEY (collectible_id) REFERENCES COLLECTIBLE(collectible_id),
    FOREIGN KEY (thumbnail_id) REFERENCES THUMBNAIL(thumbnail_id)
);

-- Tworzenie tabeli TEAM
CREATE TABLE TEAM (
    team_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    collection_id INTEGER,
    FOREIGN KEY (collection_id) REFERENCES COLLECTION(collection_id)
);

-- Tworzenie tabeli VERSION
CREATE TABLE VERSION (
    version_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL,
    start_datetime DATETIME,
    finish_datetime DATETIME,
    description TEXT,
    team_id INTEGER,
    segment_id INTEGER NOT NULL,
    FOREIGN KEY (team_id) REFERENCES TEAM(team_id),
    FOREIGN KEY (segment_id) REFERENCES SEGMENT(segment_id)
);

-- Tworzenie tabeli PHOTO
CREATE TABLE PHOTO (
    photo_id INTEGER PRIMARY KEY AUTOINCREMENT,
    file_name VARCHAR(100) NOT NULL,
    file_size FLOAT,
    version_id INTEGER NOT NULL,
    FOREIGN KEY (version_id) REFERENCES VERSION(version_id)
);

-- Tworzenie tabeli THUMBNAIL
CREATE TABLE THUMBNAIL (
    thumbnail_id INTEGER PRIMARY KEY AUTOINCREMENT,
    file_name VARCHAR(100) NOT NULL,
    collectible_id INTEGER NOT NULL,
    FOREIGN KEY (collectible_id) REFERENCES VERSION(collectible_id)
);

INSERT INTO STATUS (name) VALUES
	("nierozpoczęty"),
	("w trakcie"),
	("niezakończony"),
	("zakończony");