PRAGMA foreign_keys = OFF;

-- ============================================================
-- NETTOYAGE (DML)
-- ============================================================
-- DELETE FROM Paragraphe;
-- DELETE FROM Episode;
-- DELETE FROM Biographie;
-- DELETE FROM Participation;
-- DELETE FROM Personnage;
-- DELETE FROM Partie;
-- DELETE FROM Joueur;
-- DELETE FROM Univers;

-- ============================================================
-- CRÉATION DU SCHÉMA (DDL)
-- ============================================================

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

-- 7. Table Biographie 
CREATE TABLE IF NOT EXISTS Biographie (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT
);

-- 3. Table Partie (C'est aussi l'Aventure)
CREATE TABLE IF NOT EXISTS Partie (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT,
    resume TEXT,
    validee INTEGER DEFAULT 0, -- Booléen : 0 = faux, 1 = vrai
    jouee INTEGER DEFAULT 0, -- Booléen : 0 = faux, 1 = vrai
    univers_id INTEGER,
    FOREIGN KEY (univers_id) REFERENCES Univers(id)
);

-- 4. Table Personnage (1 Personnage -> 1 Biographie)
CREATE TABLE IF NOT EXISTS Personnage (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    profession TEXT,
    date_naissance TEXT,
    image_path TEXT,
    univers_id INTEGER,
    biographie_id INTEGER UNIQUE NOT NULL, 
    FOREIGN KEY (univers_id) REFERENCES Univers(id),
    FOREIGN KEY (biographie_id) REFERENCES Biographie(id) ON DELETE CASCADE
);

-- 5. Table d'association "Participation" 
CREATE TABLE IF NOT EXISTS Participation (
    joueur_id INTEGER,
    mj_id INTEGER,
    partie_id INTEGER,
    personnage_id INTEGER,
    PRIMARY KEY (joueur_id, mj_id, partie_id, personnage_id),
    FOREIGN KEY (mj_id) REFERENCES Joueur(id) ON DELETE CASCADE,
    FOREIGN KEY (joueur_id) REFERENCES Joueur(id) ON DELETE CASCADE,
    FOREIGN KEY (partie_id) REFERENCES Partie(id) ON DELETE CASCADE,
    FOREIGN KEY (personnage_id) REFERENCES Personnage(id) ON DELETE CASCADE
);

-- 8. Table Episode (Composition dans Biographie)
CREATE TABLE IF NOT EXISTS Episode (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT,
    joueur_valide INTEGER DEFAULT 0,
    mj_valide INTEGER DEFAULT 0,
    biographie_id INTEGER NOT NULL,
    aventure_id INTEGER, -- Reste 'aventure_id', mais c'est désormais un alias de partie_id
    FOREIGN KEY (biographie_id) REFERENCES Biographie(id) ON DELETE CASCADE,
    FOREIGN KEY (aventure_id) REFERENCES Partie(id) -- Référence la table Partie
);

-- 9. Table Paragraphe (Composition dans Episode)
CREATE TABLE IF NOT EXISTS Paragraphe (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT,
    contenu TEXT,
    is_private INTEGER DEFAULT 1,
    episode_id INTEGER NOT NULL,
    FOREIGN KEY (episode_id) REFERENCES Episode(id) ON DELETE CASCADE
);