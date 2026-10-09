INSERT INTO film (titre, realisateur, date_sortie, genre)
SELECT * FROM (VALUES
    ('Inception', 'Christopher Nolan', DATE '2010-07-16', 'ScienceFiction'),
    ('Le Voyage de Chihiro', 'Hayao Miyazaki', DATE '2001-07-20', 'Fantastique'),
    ('Parasite', 'Bong Joon-ho', DATE '2019-05-30', 'Drame')
) AS v(titre, realisateur, date_sortie, genre)
WHERE NOT EXISTS (SELECT 1 FROM film);

INSERT INTO acteur (prenom, nom)
SELECT * FROM (VALUES
    ('Leonardo', 'DiCaprio'),
    ('Marion', 'Cotillard'),
    ('Song', 'Kang-ho')
) AS v(prenom, nom)
WHERE NOT EXISTS (SELECT 1 FROM acteur);

INSERT INTO role (film_id, acteur_id, personnage)
SELECT f.id, a.id, v.personnage
FROM (VALUES
    ('Inception', 'DiCaprio', 'Dom Cobb'),
    ('Inception', 'Cotillard', 'Mal'),
    ('Parasite', 'Kang-ho', 'Kim Ki-taek')
) AS v(titre, nom, personnage)
JOIN film f ON f.titre = v.titre
JOIN acteur a ON a.nom = v.nom
WHERE NOT EXISTS (SELECT 1 FROM role);
