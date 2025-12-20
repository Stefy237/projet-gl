PRAGMA foreign_keys = OFF;

-- ============================================================
-- NETTOYAGE PRÉALABLE
-- ============================================================
DELETE FROM Paragraphe;
DELETE FROM Episode;
DELETE FROM Biographie;
DELETE FROM Participation;
DELETE FROM Personnage;
DELETE FROM Partie;
DELETE FROM Joueur;
DELETE FROM Univers;

PRAGMA foreign_keys = ON;

-- ============================================================
-- 1. UNIVERS
-- ============================================================
INSERT INTO Univers (id, nom) VALUES 
(1, 'FANTASY'), (2, 'SCIENCE_FICTION'), (3, 'HISTORIQUE'), (4, 'MODERNE'), (5, 'POST_APOCALYPTIQUE');

-- ============================================================
-- 2. JOUEURS (10 joueurs)
-- ============================================================
INSERT INTO Joueur (id, nom) VALUES 
(1, 'Alice'), (2, 'Bob'), (3, 'Charlie'), (4, 'David'), (5, 'Eve'),
(6, 'Frank'), (7, 'Grace'), (8, 'Heidi'), (9, 'Ivan'), (10, 'Judy');

-- ============================================================
-- 3. PARTIES (5 parties, une par univers)
-- Contrainte : Chaque partie a un MJ unique (Joueurs 1 à 5)
-- ============================================================
INSERT INTO Partie (id, titre, resume, validee, jouee, date, lieu, mj_id, univers_id) VALUES
(1, 'Le Grimoire Perdu', 'Quête épique', 1, 0, '2025-12-20', 'Donjon', 1, 1),
(2, 'Station Delta', 'Survie spatiale', 1, 0, '2025-12-21', 'Espace', 2, 2),
(3, 'Le Secret du Roi', 'Complot à la cour', 1, 0, '2025-12-22', 'Versailles', 3, 3),
(4, 'Braquage à l''italienne', 'Crime moderne', 1, 0, '2025-12-23', 'Milan', 4, 4),
(5, 'Terres Brûlées', 'Route du désert', 1, 0, '2025-12-24', 'Désert', 5, 5);

-- ============================================================
-- 4. BIOGRAPHIES (40 entrées : 10 joueurs * 4 personnages)
-- ============================================================
WITH RECURSIVE cnt(n) AS (
    SELECT 1 UNION ALL SELECT n+1 FROM cnt WHERE n < 40
)
INSERT INTO Biographie (id, titre)
SELECT n, 'Chronique N°' || n FROM cnt;

-- ============================================================
-- 5. PERSONNAGES (40 personnages : 4 par joueur)
-- ============================================================
-- Logique : Joueur 1 a les persos 1-4, Joueur 2 a 5-8, etc.
INSERT INTO Personnage (id, nom, profession, univers_id, biographie_id)
SELECT 
    id,
    'Héros ' || id,
    'Aventurier',
    ((id-1) % 5) + 1, -- Répartition cyclique des univers
    id                -- Relation 1-1 avec Biographie
FROM Biographie;

-- ============================================================
-- 6. PARTICIPATION (5 personnages par partie)
-- Contraintes respectées :
-- - 5 persos par partie
-- - MJ n'a pas de perso dans la partie
-- - Un perso n'est que dans une seule partie (non jouée)
-- ============================================================
-- Partie 1 (MJ: 1) : Persos des joueurs 6, 7, 8, 9, 10 (Persos ID: 21, 25, 29, 33, 37)
-- Partie 2 (MJ: 2) : Persos des joueurs 1, 6, 7, 8, 9  (Persos ID: 1, 22, 26, 30, 34)
-- Partie 3 (MJ: 3) : Persos des joueurs 1, 2, 6, 7, 8  (Persos ID: 2, 5, 23, 27, 31)
-- Partie 4 (MJ: 4) : Persos des joueurs 1, 2, 3, 6, 7  (Persos ID: 3, 6, 9, 24, 28)
-- Partie 5 (MJ: 5) : Persos des joueurs 1, 2, 3, 4, 6  (Persos ID: 4, 7, 10, 13, 25)

INSERT INTO Participation (partie_id, personnage_id, joueur_id, mj_id) VALUES
-- Partie 1 (MJ Alice ID 1)
(1, 21, 6, 1), (1, 25, 7, 1), (1, 29, 8, 1), (1, 33, 9, 1), (1, 37, 10, 1),
-- Partie 2 (MJ Bob ID 2)
(2, 1, 1, 2), (2, 22, 6, 2), (2, 26, 7, 2), (2, 30, 8, 2), (2, 34, 9, 2),
-- Partie 3 (MJ Charlie ID 3)
(3, 2, 1, 3), (3, 5, 2, 3), (3, 23, 6, 3), (3, 27, 7, 3), (3, 31, 8, 3),
-- Partie 4 (MJ David ID 4)
(4, 3, 1, 4), (4, 6, 2, 4), (4, 9, 3, 4), (4, 24, 6, 4), (4, 28, 7, 4),
-- Partie 5 (MJ Eve ID 5)
(5, 4, 1, 5), (5, 7, 2, 5), (5, 10, 3, 5), (5, 13, 4, 5), (5, 32, 8, 5);

-- ============================================================
-- 7. ÉPISODES (Un par biographie/personnage)
-- ============================================================
INSERT INTO Episode (id, titre, biographie_id, aventure_id)
SELECT 
    id, 
    'Introduction de ' || (SELECT nom FROM Personnage WHERE id = biographie_id),
    id,
    (SELECT partie_id FROM Participation WHERE personnage_id = id LIMIT 1)
FROM Biographie;

-- ============================================================
-- 8. PARAGRAPHES (Un public par biographie)
-- Contrainte : au moins un paragraphe public par biographie
-- ============================================================
INSERT INTO Paragraphe (titre, contenu, is_private, episode_id)
SELECT 
    'Origines',
    'Ceci est le récit public du personnage.',
    0, -- 0 = Public (conformément à l'énoncé)
    id
FROM Episode;

PRAGMA foreign_keys = ON;