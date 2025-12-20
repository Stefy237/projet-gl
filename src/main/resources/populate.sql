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
-- 2. JOUEURS
-- ============================================================
INSERT INTO Joueur (id, nom) VALUES 
(1, 'Alice'), (2, 'Bob'), (3, 'Charlie'), (4, 'David'), (5, 'Eve'),
(6, 'Frank'), (7, 'Grace'), (8, 'Heidi'), (9, 'Ivan'), (10, 'Judy');

-- ============================================================
-- 3. PARTIES
-- ============================================================
INSERT INTO Partie (id, titre, resume, validee, jouee, date, lieu, mj_id, univers_id) VALUES
(1, 'Le Grimoire Perdu', 'Quête épique', 1, 0, '2025-12-20', 'Donjon', 1, 1),
(2, 'Station Delta', 'Survie spatiale', 1, 0, '2025-12-21', 'Espace', 2, 2),
(3, 'Le Secret du Roi', 'Complot à la cour', 1, 0, '2025-12-22', 'Versailles', 3, 3),
(4, 'Braquage à l''italienne', 'Crime moderne', 1, 0, '2025-12-23', 'Milan', 4, 4),
(5, 'Terres Brûlées', 'Route du désert', 1, 0, '2025-12-24', 'Désert', 5, 5);

-- ============================================================
-- 4. BIOGRAPHIES (40 entrées)
-- ============================================================
WITH RECURSIVE cnt(n) AS (
    SELECT 1 UNION ALL SELECT n+1 FROM cnt WHERE n < 40
)
INSERT INTO Biographie (id, titre)
SELECT n, 'Chronique de ' || n FROM cnt;

-- ============================================================
-- 5. PERSONNAGES
-- ============================================================
INSERT INTO Personnage (id, nom, profession, univers_id, biographie_id)
SELECT id, 'Héros ' || id, 'Aventurier', ((id-1) % 5) + 1, id FROM Biographie;

-- ============================================================
-- 6. PARTICIPATION
-- ============================================================
INSERT INTO Participation (partie_id, personnage_id, joueur_id, mj_id) VALUES
(1, 21, 6, 1), (1, 25, 7, 1), (1, 29, 8, 1), (1, 33, 9, 1), (1, 37, 10, 1),
(2, 1, 1, 2), (2, 22, 6, 2), (2, 26, 7, 2), (2, 30, 8, 2), (2, 34, 9, 2),
(3, 2, 1, 3), (3, 5, 2, 3), (3, 23, 6, 3), (3, 27, 7, 3), (3, 31, 8, 3),
(4, 3, 1, 4), (4, 6, 2, 4), (4, 9, 3, 4), (4, 24, 6, 4), (4, 28, 7, 4),
(5, 4, 1, 5), (5, 7, 2, 5), (5, 10, 3, 5), (5, 13, 4, 5), (5, 32, 8, 5);

-- ============================================================
-- 7. ÉPISODES (2 par biographie : 1 validé, 1 en cours)
-- ============================================================
-- Premier épisode : Validé (joueur_valide=1, mj_valide=1)
INSERT INTO Episode (id, titre, joueur_valide, mj_valide, biographie_id, aventure_id)
SELECT 
    id, 
    'Chapitre 1 : Les Origines de ' || (SELECT nom FROM Personnage WHERE id = biographie_id),
    1, 1, -- Terminé et validé
    id,
    (SELECT partie_id FROM Participation WHERE personnage_id = id LIMIT 1)
FROM Biographie;

-- Second épisode : En cours (joueur_valide=0, mj_valide=0)
INSERT INTO Episode (id, titre, joueur_valide, mj_valide, biographie_id, aventure_id)
SELECT 
    id + 100, -- ID décalé pour éviter les conflits
    'Chapitre 2 : L''Aventure continue pour ' || (SELECT nom FROM Personnage WHERE id = biographie_id),
    0, 0, -- En cours d'écriture
    id,
    (SELECT partie_id FROM Participation WHERE personnage_id = id LIMIT 1)
FROM Biographie;

-- ============================================================
-- 8. PARAGRAPHES (4 par épisode, mix public/privé)
-- ============================================================
-- Génération de 4 paragraphes pour chaque épisode créé ci-dessus
INSERT INTO Paragraphe (titre, contenu, is_private, episode_id)
SELECT 
    'Section ' || n.idx,
    'Détails du récit numéro ' || n.idx,
    CASE WHEN n.idx % 2 = 0 THEN 1 ELSE 0 END, -- Alterne privé (1) et public (0)
    e.id
FROM Episode e
CROSS JOIN (
    SELECT 1 AS idx UNION SELECT 2 UNION SELECT 3 
) n;

PRAGMA foreign_keys = ON;