PRAGMA foreign_keys = OFF;

-- ============================================================
-- NETTOYAGE DES DONNÉES (DML)
-- ============================================================
DELETE FROM Paragraphe;
DELETE FROM Episode;
DELETE FROM Biographie;
DELETE FROM Participation;
DELETE FROM Personnage;
DELETE FROM Partie;
DELETE FROM Joueur;
DELETE FROM Univers;

-- ============================================================
-- PEUPLEMENT DE LA BD
-- ============================================================

-- 1. UNIVERS
INSERT INTO Univers (id, nom) VALUES 
(1, 'FANTASY'), (2, 'SCIENCE_FICTION'), (3, 'HISTORIQUE'), (4, 'MODERNE'), (5, 'POST_APOCALYPTIQUE');

-- 2. JOUEURS (10 joueurs)
INSERT INTO Joueur (id, nom) VALUES 
(1, 'Alice'), (2, 'Bob'), (3, 'Charlie'), (4, 'David'), (5, 'Eve'),
(6, 'Frank'), (7, 'Grace'), (8, 'Heidi'), (9, 'Ivan'), (10, 'Judy');

-- 3. PARTIES / AVENTURES (20 entrées)
-- Note : mj_id est obligatoire dans le nouveau schéma.
INSERT INTO Partie (id, titre, resume, jouee, univers_id, validee, mj_id, date, lieu) VALUES
(1, 'La Quête de l''Anneau', 'Un classique indémodable.', 1, 1, 1, 1, '2025-01-01', 'Terre du Milieu'),
(2, 'Les Cavernes du Chaos', 'Exploration de donjon.', 0, 1, 0, 1, '2025-02-10', 'Donjon Noir'),
(3, 'Le Roi Sombre', 'Campagne politique.', 1, 1, 1, 2, '2025-03-15', 'Capitale'),
(4, 'Chasse aux Dragons', 'Gros monstres et trésors.', 0, 1, 0, 2, '2025-04-20', 'Montagne de Feu'),
(5, 'Neon City Blues', 'Enquête sous la pluie acide.', 1, 2, 1, 3, '2025-05-05', 'Tokyo 2099'),
(6, 'Corpo War', 'Sabotage industriel.', 0, 2, 0, 3, '2025-06-12', 'Orbitale'),
(7, 'Netrunner Legacy', 'Tout se passe dans le cyberespace.', 1, 2, 0, 4, '2025-07-20', 'Le Réseau'),
(8, 'Blade Runner Dreams', 'Chasse aux androïdes.', 0, 2, 0, 4, '2025-08-15', 'Los Angeles'),
(9, 'L''Appel de Cthulhu', 'Tout le monde meurt à la fin.', 1, 3, 1, 5, '2025-09-01', 'Arkham'),
(10, 'Cauchemar à Innsmouth', 'Des poissons bizarres.', 0, 3, 0, 5, '2025-10-10', 'Port d''Innsmouth'),
(11, 'Le Roi en Jaune', 'Une pièce de théâtre maudite.', 1, 3, 1, 6, '2025-11-05', 'Paris 1890'),
(12, 'La Montagne Hallucinée', 'Il fait froid.', 0, 3, 0, 6, '2025-12-01', 'Antarctique'),
(13, 'Star Wars: Bordure Ext.', 'Contrebande spatiale.', 1, 4, 1, 7, '2026-01-10', 'Tatooine'),
(14, 'Dune: Arrakis', 'Le culte de l''épice.', 0, 4, 0, 7, '2026-02-20', 'Arrakeen'),
(15, 'Mass Effect: Relay', 'Diplomatie galactique.', 1, 4, 1, 8, '2026-03-05', 'La Citadelle'),
(16, 'Alien Isolation', 'Survival horror dans l''espace.', 0, 4, 0, 8, '2026-04-12', 'Svastopol'),
(17, 'Mad Max: Fury Road', 'Voitures et sable.', 1, 5, 1, 9, '2026-05-20', 'Désert'),
(18, 'Fallout: New Vegas', 'Le désert radioactif.', 0, 5, 0, 9, '2026-06-15', 'Mojave'),
(19, 'Zombie Survival', 'Survivre jour après jour.', 1, 5, 1, 10, '2026-07-01', 'Ville Morte'),
(20, 'Metro 2033', 'Vie dans les tunnels.', 0, 5, 0, 10, '2026-08-10', 'Moscou');

-- 4. BIOGRAPHIES (30 entrées)
WITH RECURSIVE cnt(n) AS (
    SELECT 1 UNION ALL SELECT n+1 FROM cnt WHERE n < 30
)
INSERT INTO Biographie (id, titre)
SELECT n, 'Chronique de la vie N°' || n FROM cnt;

-- 5. PERSONNAGES (30 personnages liés aux biographies)
INSERT INTO Personnage (id, nom, profession, date_naissance, univers_id, biographie_id)
SELECT 
    id,
    CASE ((id-1)%5) 
        WHEN 0 THEN 'Guerrier ' || id WHEN 1 THEN 'Mage ' || id 
        WHEN 2 THEN 'Voleur ' || id WHEN 3 THEN 'Prêtre ' || id 
        ELSE 'Barde ' || id END,
    'Aventurier', '1990-01-01',
    ((id-1)/6)+1, -- Répartit sur les 5 univers
    id -- Relation 1-1 avec Biographie
FROM Biographie;

-- Mise à jour des titres de biographies avec les noms des personnages
UPDATE Biographie SET titre = 'Histoire de ' || (SELECT nom FROM Personnage WHERE Personnage.biographie_id = Biographie.id);

-- 6. PARTICIPATION (Lien entre Joueurs, Parties et Personnages)
-- On fait participer les 10 joueurs à différentes parties avec leurs personnages
INSERT INTO Participation (joueur_id, mj_id, partie_id, personnage_id)
SELECT 
    ((id-1) % 10) + 1, -- Joueur (1 à 10)
    (SELECT mj_id FROM Partie WHERE id = (((id-1) % 20) + 1)), -- MJ de la partie choisie
    ((id-1) % 20) + 1, -- Partie (1 à 20)
    id                -- Personnage (1 à 30)
FROM Personnage;

-- 7. ÉPISODES (5 chapitres par personnage)
INSERT INTO Episode (titre, joueur_valide, mj_valide, date_creation, biographie_id, aventure_id)
SELECT 
    'Chapitre ' || numbers.n || ' : Les prémices', 
    1, 1, '2025-12-17',
    p.biographie_id,
    part.partie_id
FROM Personnage p
JOIN Participation part ON p.id = part.personnage_id
CROSS JOIN (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) AS numbers;

-- 8. PARAGRAPHES (3 paragraphes par épisode)
INSERT INTO Paragraphe (titre, contenu, is_private, episode_id)
SELECT 
    'Paragraphe ' || numbers.n, 
    'Voici le récit épique de l''action numéro ' || numbers.n || '.',
    CASE WHEN numbers.n = 3 THEN 1 ELSE 0 END, -- Le 3ème paragraphe est privé
    e.id
FROM Episode e
CROSS JOIN (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3) AS numbers;

PRAGMA foreign_keys = ON;