-- 1. UNIVERS
INSERT INTO Univers (id, nom) VALUES 
(1, 'FANTASY'), (2, 'SCIENCE_FICTION'), (3, 'HISTORIQUE'), (4, 'MODERNE'), (5, 'POST_APOCALYPTIQUE');

-- 2. JOUEURS
INSERT INTO Joueur (id, nom) VALUES 
(1, 'Alice'), (2, 'Bob'), (3, 'Charlie'), (4, 'David'), (5, 'Eve'),
(6, 'Frank'), (7, 'Grace'), (8, 'Heidi'), (9, 'Ivan'), (10, 'Judy');

-- 3. PARTIES (20 entrées)
INSERT INTO Partie (titre, resume, jouee, univers_id, validee) VALUES
-- Univ 1
('La Quête de l''Anneau', 'Un classique indémodable.', 1, 1, 1),
('Les Cavernes du Chaos', 'Exploration de donjon.', 0, 1, 0),     
('Le Roi Sombre', 'Campagne politique.', 1, 1, 1),              
('Chasse aux Dragons', 'Gros monstres et trésors.', 0, 1, 0),     
-- Univ 2
('Neon City Blues', 'Enquête sous la pluie acide.', 1, 2, 1),
('Corpo War', 'Sabotage industriel.', 0, 2, 0),                   
('Netrunner Legacy', 'Tout se passe dans le cyberespace.', 1, 2, 0), 
('Blade Runner Dreams', 'Chasse aux androïdes.', 0, 2, 0),       
-- Univ 3
('L''Appel de Cthulhu', 'Tout le monde meurt à la fin.', 1, 3, 1), 
('Cauchemar à Innsmouth', 'Des poissons bizarres.', 0, 3, 0),    
('Le Roi en Jaune', 'Une pièce de théâtre maudite.', 1, 3, 1),
('La Montagne Hallucinée', 'Il fait froid.', 0, 3, 0),           
-- Univ 4
('Star Wars: Bordure Ext.', 'Contrebande spatiale.', 1, 4, 1),
('Dune: Arrakis', 'Le culte de l''épice.', 0, 4, 0),             
('Mass Effect: Relay', 'Diplomatie galactique.', 1, 4, 1),   
('Alien Isolation', 'Survival horror dans l''espace.', 0, 4, 0), 
-- Univ 5
('Mad Max: Fury Road', 'Voitures et sable.', 1, 5, 1),       
('Fallout: New Vegas', 'Le désert radioactif.', 0, 5, 0),        
('Zombie Survival', 'Survivre jour après jour.', 1, 5, 1),   
('Metro 2033', 'Vie dans les tunnels.', 0, 5, 0);


-- 4. BIOGRAPHIES (30 entrées)
INSERT INTO Biographie (id, titre)
SELECT ROWID, 'Biographie temporaire N°' || ROWID 
FROM (SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
    UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
    UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25 UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30) LIMIT 30;

-- 5. PERSONNAGES (30 entrées)
WITH PersonnageData AS (
    SELECT id AS row_num,
        CASE id WHEN 1 THEN 'Aragorn' WHEN 7 THEN 'V' WHEN 13 THEN 'Detective Malone' WHEN 19 THEN 'Han Solo' WHEN 25 THEN 'Max' ELSE 'Autre' END AS nom,
        CASE WHEN id <= 6 THEN 1 WHEN id <= 12 THEN 2 WHEN id <= 18 THEN 3 WHEN id <= 24 THEN 4 ELSE 5 END AS univers_id
    FROM (SELECT ROWID AS id FROM Biographie LIMIT 30)
)
INSERT INTO Personnage (id, nom, profession, date_naissance, univers_id, biogragie_id)
SELECT PD.row_num, PD.nom, PD.nom || ' profession', '2000-01-01', PD.univers_id, PD.row_num
FROM PersonnageData PD;

UPDATE Biographie SET titre = 'La vie de ' || (SELECT nom FROM Personnage WHERE Personnage.id = Biographie.id);


-- 6. PARTICIPATION (Association Joueur-Personnage-Partie)
-- Chaque joueur (J.id) participe à 5 parties différentes (P.id) avec 5 personnages différents (PERSO.id)
DELETE FROM Participation;

-- ==========================================================
-- LOGIQUE D'INSERTION : JOINTURE CROISÉE ET FILTRAGE (50 lignes garanties)
-- ==========================================================
-- Nous allons joindre Joueur (J) avec une séquence numérique (N) de 0 à 4.
-- Ceci garantit 5 lignes de base pour chaque joueur (10 joueurs * 5 lignes = 50 lignes).
WITH Séquence5 (n) AS (
    SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
)
INSERT INTO Participation (joueur_id, mj_id, partie_id, personnage_id)
SELECT
    J.id AS joueur_id,

    -- 1. MJ ID: Fait tourner l'ID du MJ parmi les 10 joueurs
    -- MJ_ID = (ID_joueur + N) % 10 + 1 (Assure que l'ID est toujours entre 1 et 10)
    ((J.id + S.n) % 10) + 1 AS mj_id,

    -- 2. PARTIE ID: Fait tourner l'ID de la Partie parmi les 20 parties disponibles
    -- PARTIE_ID = (ID_joueur + N * 2) % 20 + 1
    -- On multiplie par 2 pour que les IDs de partie progressent plus vite et évitent les doublons immédiats
    ((J.id + S.n * 2) % 20) + 1 AS partie_id,

    -- 3. PERSONNAGE ID: Fait tourner l'ID du Personnage parmi les 30 personnages
    -- PERSO_ID = (ID_joueur * 5 + N) % 30 + 1
    ((J.id * 5 + S.n) % 30) + 1 AS personnage_id
    
FROM Joueur J
CROSS JOIN Séquence5 S
-- WHERE pour s'assurer qu'un joueur n'est pas son propre MJ, 
-- mais pour la robustesse des données de test, nous le permettons.
;

-- 7. ÉPISODES (150 épisodes, 5 par Biographie)
INSERT INTO Episode (titre, biographie_id, aventure_id)
SELECT 
    'Chapitre ' || numbers.n || ' : Le Début de la fin', 
    b.id,
    -- CORRECTION D'INSERTION : aventure_id = partie_id du personnage
    (SELECT MIN(p.partie_id) FROM Participation p WHERE p.personnage_id = b.id) 
FROM Biographie b
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
) AS numbers;

-- 8. PARAGRAPHES (750 paragraphes, 5 par Épisode)
INSERT INTO Paragraphe (titre, contenu, is_private, episode_id)
SELECT 
    'Section ' || numbers.n, 
    'Contenu détaillé du paragraphe ' || numbers.n,
    (numbers.n % 2),
    e.id
FROM Episode e
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
) AS numbers;


PRAGMA foreign_keys = ON;