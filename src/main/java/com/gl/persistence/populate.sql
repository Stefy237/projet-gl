-- DÉBUT DU SCRIPT MASSIF
PRAGMA foreign_keys = OFF; -- On désactive pour aller vite, on réactive à la fin

-- Nettoyage préalable (Optionnel, retirez si vous utilisez le mode Reset Java)
DELETE FROM Paragraphe;
DELETE FROM Episode;
DELETE FROM Biographie;
DELETE FROM Personnage_Aventure;
DELETE FROM Aventure;
DELETE FROM Participation;
DELETE FROM Personnage;
DELETE FROM Partie;
DELETE FROM Joueur;
DELETE FROM Univers;

-- ============================================================
-- 1. LES 5 UNIVERS
-- ============================================================
INSERT INTO Univers (id, nom) VALUES 
(1, 'Médiéval-Fantastique'),
(2, 'Cyberpunk 2077'),
(3, 'Horreur Lovecraftienne'),
(4, 'Space Opera'),
(5, 'Post-Apocalyptique');

-- ============================================================
-- 2. LES 10 JOUEURS
-- ============================================================
INSERT INTO Joueur (id, nom) VALUES 
(1, 'Alice'), (2, 'Bob'), (3, 'Charlie'), (4, 'David'), (5, 'Eve'),
(6, 'Frank'), (7, 'Grace'), (8, 'Heidi'), (9, 'Ivan'), (10, 'Judy');

-- ============================================================
-- 3. LES 20 PARTIES (4 par univers)
-- ============================================================
INSERT INTO Partie (titre, resume, jouee, univers_id) VALUES
-- Univ 1
('La Quête de l''Anneau', 'Un classique indémodable.', 1, 1),
('Les Cavernes du Chaos', 'Exploration de donjon.', 0, 1),
('Le Roi Sombre', 'Campagne politique.', 1, 1),
('Chasse aux Dragons', 'Gros monstres et trésors.', 0, 1),
-- Univ 2
('Neon City Blues', 'Enquête sous la pluie acide.', 1, 2),
('Corpo War', 'Sabotage industriel.', 0, 2),
('Netrunner Legacy', 'Tout se passe dans le cyberespace.', 1, 2),
('Blade Runner Dreams', 'Chasse aux androïdes.', 0, 2),
-- Univ 3
('L''Appel de Cthulhu', 'Tout le monde meurt à la fin.', 1, 3),
('Cauchemar à Innsmouth', 'Des poissons bizarres.', 0, 3),
('Le Roi en Jaune', 'Une pièce de théâtre maudite.', 1, 3),
('La Montagne Hallucinée', 'Il fait froid.', 0, 3),
-- Univ 4
('Star Wars: Bordure Ext.', 'Contrebande spatiale.', 1, 4),
('Dune: Arrakis', 'Le culte de l''épice.', 0, 4),
('Mass Effect: Relay', 'Diplomatie galactique.', 1, 4),
('Alien Isolation', 'Survival horror dans l''espace.', 0, 4),
-- Univ 5
('Mad Max: Fury Road', 'Voitures et sable.', 1, 5),
('Fallout: New Vegas', 'Le désert radioactif.', 0, 5),
('Zombie Survival', 'Survivre jour après jour.', 1, 5),
('Metro 2033', 'Vie dans les tunnels.', 0, 5);

-- ============================================================
-- 4. LES 30 PERSONNAGES
-- ============================================================
INSERT INTO Personnage (nom, profession, date_naissance, univers_id) VALUES
-- Univ 1
('Aragorn', 'Rôdeur', '1240-01-01', 1), ('Gandalf', 'Magicien', '0001-01-01', 1),
('Legolas', 'Archer', '1000-01-01', 1), ('Gimli', 'Guerrier', '1100-01-01', 1),
('Frodo', 'Voleur', '1280-09-22', 1), ('Sam', 'Jardinier', '1283-05-01', 1),
-- Univ 2
('V', 'Mercenaire', '2040-12-10', 2), ('Johnny', 'Rockerboy', '1988-11-16', 2),
('Alt', 'Netrunner', '2000-01-01', 2), ('Takemura', 'Soldat Corp', '2020-05-05', 2),
('Judy', 'Techie', '2050-03-03', 2), ('Panam', 'Nomade', '2048-08-08', 2),
-- Univ 3
('Detective Malone', 'Détective', '1890-07-14', 3), ('Prof. Armitage', 'Erudit', '1870-02-20', 3),
('Cultiste Fou', 'Adorateur', '1900-01-01', 3), ('Journaliste Curieux', 'Enquêteur', '1895-06-06', 3),
('Dr. West', 'Médecin', '1885-11-11', 3), ('Artiste Maudit', 'Peintre', '1892-04-04', 3),
-- Univ 4
('Han Solo', 'Contrebandier', '2980-01-01', 4), ('Leia', 'Princesse', '2980-01-01', 4),
('Luke', 'Jedi', '2980-01-01', 4), ('Chewbacca', 'Pilote', '2900-01-01', 4),
('Spock', 'Officier', '2230-01-06', 4), ('Kirk', 'Capitaine', '2233-03-22', 4),
-- Univ 5
('Max', 'Conducteur', '1990-01-01', 5), ('Furiosa', 'Imperator', '1995-01-01', 5),
('Joel', 'Survivant', '1980-09-26', 5), ('Ellie', 'Adolescente', '2019-01-01', 5),
('Artyom', 'Ranger', '2013-03-31', 5), ('Miller', 'Colonel', '1970-10-10', 5);

-- ============================================================
-- 5. PARTICIPATION (Génération procédurale)
-- ============================================================
-- On associe chaque Joueur à plusieurs parties et personnages via un produit cartésien filtré
-- pour éviter de tout écrire à la main tout en créant du volume.
INSERT INTO Participation (joueur_id, partie_id, personnage_id)
SELECT 
    j.id, 
    p.id, 
    perso.id
FROM Joueur j
JOIN Partie p ON p.id = (j.id + j.id) -- Logique arbitraire pour distribuer
JOIN Personnage perso ON perso.id = j.id
WHERE perso.univers_id = p.univers_id; -- On s'assure que le perso correspond à l'univers de la partie

-- ============================================================
-- 6. BIOGRAPHIES (1 par personnage)
-- ============================================================
INSERT INTO Biographie (titre, personnage_id)
SELECT 'La vie de ' || nom, id FROM Personnage;

-- ============================================================
-- 7. ÉPISODES (Génération : 5 épisodes par Biographie)
-- ============================================================
-- On utilise une table temporaire virtuelle (CTE) pour générer les chiffres 1 à 5
-- Puis on fait un CROSS JOIN avec les biographies.
INSERT INTO Episode (titre, biographie_id)
SELECT 
    'Chapitre ' || numbers.n || ' : Le Début de la fin', 
    b.id
FROM Biographie b
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
) AS numbers;

-- À ce stade, nous avons 30 persos * 5 épisodes = 150 épisodes créés.

-- ============================================================
-- 8. PARAGRAPHES (Génération : 5 paragraphes par Épisode)
-- ============================================================
-- Même technique : CROSS JOIN pour créer 5 paragraphes pour chacun des 150 épisodes.
-- Total = 750 paragraphes.
INSERT INTO Paragraphe (titre, contenu, is_private, episode_id)
SELECT 
    'Section ' || numbers.n, 
    CASE numbers.n 
        WHEN 1 THEN 'Tout a commencé par une nuit sombre et orageuse...'
        WHEN 2 THEN 'Le héros se leva et prit son arme.'
        WHEN 3 THEN 'Un bruit soudain se fit entendre derrière la porte.'
        WHEN 4 THEN 'Le combat fut rude, mais la victoire proche.'
        ELSE 'Finalement, le calme revint, pour l''instant.'
    END,
    (numbers.n % 2), -- Alterne entre Privé (1) et Public (0)
    e.id
FROM Episode e
CROSS JOIN (
    SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
) AS numbers;

-- ============================================================
-- 9. AVENTURES ET LIENS
-- ============================================================
INSERT INTO Aventure (nom, partie_id) 
SELECT 'Aventure Principale de ' || titre, id FROM Partie;

-- Lier les personnages à l'aventure de leur univers (simplifié)
INSERT INTO Personnage_Aventure (personnage_id, aventure_id)
SELECT p.id, a.id
FROM Personnage p
JOIN Aventure a ON a.id = p.univers_id; -- Logique simplifiée pour peupler

PRAGMA foreign_keys = ON;