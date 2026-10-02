INSERT INTO film (titre, realisateur, date_sortie, genre) VALUES ('Inception', 'Christopher Nolan', '2010-07-16', 'ScienceFiction');
INSERT INTO film (titre, realisateur, date_sortie, genre) VALUES ('Le Voyage de Chihiro', 'Hayao Miyazaki', '2001-07-20', 'Fantastique');
INSERT INTO film (titre, realisateur, date_sortie, genre) VALUES ('Parasite', 'Bong Joon-ho', '2019-05-30', 'Drame');
INSERT INTO acteur (prenom, nom) VALUES ('Leonardo', 'DiCaprio');
INSERT INTO acteur (prenom, nom) VALUES ('Marion', 'Cotillard');
INSERT INTO acteur (prenom, nom) VALUES ('Song', 'Kang-ho');
INSERT INTO film_acteur (film_id, acteur_id) VALUES (1, 1);
INSERT INTO film_acteur (film_id, acteur_id) VALUES (1, 2);
INSERT INTO film_acteur (film_id, acteur_id) VALUES (3, 3);