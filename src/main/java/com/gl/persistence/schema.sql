-- Activation des clés étrangères (nécessaire sur SQLite)
PRAGMA foreign_keys = ON;

-- 1. Table pour l'Enum Univers
CREATE TABLE IF NOT EXISTS Univers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL UNIQUE
);

-- 2. Table Joueur
CREATE TABLE IF NOT EXISTS Joueur (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL
);

-- 3. Table Partie
CREATE TABLE IF NOT EXISTS Partie (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT,
    resume TEXT,
    jouee INTEGER DEFAULT 0, -- Booléen : 0 = faux, 1 = vrai
    univers_id INTEGER,
    FOREIGN KEY (univers_id) REFERENCES Univers(id)
);

-- 4. Table Personnage
CREATE TABLE IF NOT EXISTS Personnage (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    profession TEXT,
    date_naissance TEXT, -- Format YYYY-MM-DD
    image_path TEXT,
    univers_id INTEGER,
    FOREIGN KEY (univers_id) REFERENCES Univers(id)
);

-- 5. Table d'association "Participation" 
-- Relie un Joueur, un Personnage et une Partie
CREATE TABLE IF NOT EXISTS Participation (
    joueur_id INTEGER,
    partie_id INTEGER,
    personnage_id INTEGER,
    PRIMARY KEY (joueur_id, partie_id, personnage_id),
    FOREIGN KEY (joueur_id) REFERENCES Joueur(id) ON DELETE CASCADE,
    FOREIGN KEY (partie_id) REFERENCES Partie(id) ON DELETE CASCADE,
    FOREIGN KEY (personnage_id) REFERENCES Personnage(id) ON DELETE CASCADE
);

-- 6. Table Aventure
CREATE TABLE IF NOT EXISTS Aventure (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT,
    partie_id INTEGER, -- Une partie se rapporte à une aventure
    FOREIGN KEY (partie_id) REFERENCES Partie(id)
);

-- Relation Many-to-Many entre Personnage et Aventure
CREATE TABLE IF NOT EXISTS Personnage_Aventure (
    personnage_id INTEGER,
    aventure_id INTEGER,
    PRIMARY KEY (personnage_id, aventure_id),
    FOREIGN KEY (personnage_id) REFERENCES Personnage(id) ON DELETE CASCADE,
    FOREIGN KEY (aventure_id) REFERENCES Aventure(id) ON DELETE CASCADE
);

-- 7. Table Biographie (1 Personnage -> 1 Biographie)
CREATE TABLE IF NOT EXISTS Biographie (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT,
    personnage_id INTEGER UNIQUE NOT NULL, -- UNIQUE assure la relation 1-1
    FOREIGN KEY (personnage_id) REFERENCES Personnage(id) ON DELETE CASCADE
);

-- 8. Table Episode (Composition dans Biographie)
CREATE TABLE IF NOT EXISTS Episode (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT,
    biographie_id INTEGER NOT NULL,
    aventure_id INTEGER, -- Lien vers Aventure
    FOREIGN KEY (biographie_id) REFERENCES Biographie(id) ON DELETE CASCADE,
    FOREIGN KEY (aventure_id) REFERENCES Aventure(id)
);

-- 9. Table Paragraphe (Composition dans Episode)
CREATE TABLE IF NOT EXISTS Paragraphe (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT,
    contenu TEXT,
    is_private INTEGER DEFAULT 1, -- Booléen
    episode_id INTEGER NOT NULL,
    FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE CASCADE
);