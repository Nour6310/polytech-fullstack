# Bibliothèque de films — API REST

> La présentation complète du projet (installation, front Angular, fonctionnalités, tableau de
> tous les endpoints, tags) se trouve dans le [README à la racine du dépôt](../../README.md).
> Ce fichier résume uniquement ce qui concerne l'API.

API REST développée avec Spring Boot 4.1.1 (JDK 26) pour gérer des films, des acteurs, le
personnage joué par chaque acteur dans un film (entité `Role`) et des commentaires. Les données
sont stockées dans PostgreSQL avec Spring Data JPA.

## Prérequis

- JDK 26 ;
- PostgreSQL ;
- Gradle n'est pas à installer : le wrapper `./gradlew` est fourni (Gradle 9.7.1).

## Préparer la base de données

```bash
sudo -u postgres createdb films-db
```

Mettre ensuite le mot de passe de **votre** utilisateur PostgreSQL dans
`spring.datasource.password` (fichier `src/main/resources/application.properties`).

Pour repartir d'une base propre (par exemple si elle contient encore l'ancienne table
`film_acteur` du TD 2) :

```bash
psql -U postgres -d films-db -c "DROP TABLE IF EXISTS commentaire, role, film_acteur, film, acteur CASCADE;"
```

## Configuration

Tout se trouve dans `src/main/resources/application.properties` :

| Propriété | Rôle |
|---|---|
| `spring.datasource.url` | adresse de la base : `jdbc:postgresql://localhost:5432/films-db` |
| `spring.datasource.username` / `password` | identifiants PostgreSQL (`postgres`), à adapter à votre installation |
| `spring.jpa.hibernate.ddl-auto=update` | Hibernate crée les tables manquantes au démarrage et ne supprime jamais les données |
| `spring.jpa.show-sql=true` | affiche dans la console chaque requête SQL exécutée |
| `spring.jpa.open-in-view=false` | la session Hibernate se ferme à la fin de la transaction |
| `spring.sql.init.mode=always` | exécute `data.sql` à chaque démarrage |
| `spring.jpa.defer-datasource-initialization=true` | exécute `data.sql` après la création des tables |
| `app.cors.allowed-origins` | origine autorisée à appeler l'API depuis un navigateur (`http://localhost:4200`) |

`src/main/resources/data.sql` insère 3 films, 3 acteurs et 3 rôles. Chaque insertion est
protégée par `WHERE NOT EXISTS` : elle n'a lieu que si la table est vide, ce qui permet de
rejouer le fichier à chaque démarrage sans créer de doublons. Les données sont donc conservées
entre deux redémarrages.

## Démarrer l'application

```bash
./gradlew bootRun        # Windows : gradlew.bat bootRun
```

L'application est prête quand la console affiche `Started BackApplication`. Elle écoute sur
`http://localhost:8080`. Pour l'arrêter : `Ctrl+C`.

## Tester l'API

Les requêtes de test sont dans `http/` : `films.http` (films, acteurs d'un film, commentaires)
et `acteurs.http` (acteurs). Avec l'extension VS Code **REST Client**, cliquer sur
**Send Request** au-dessus de chaque requête. Comme la base n'est plus vidée à l'arrêt, les
identifiants utilisés dans ces fichiers correspondent à une base propre (voir plus haut).

## Endpoints (résumé)

| Ressource | Endpoints |
|---|---|
| Films | `GET /films`, `GET /films/{id}`, `POST /films`, `PUT /films/{id}`, `DELETE /films/{id}` |
| Film ↔ acteur | `GET /films/{id}/acteurs`, `POST /films/{id}/acteurs/{acteurId}` (corps facultatif `{ "personnage": "..." }`), `DELETE /films/{id}/acteurs/{acteurId}` |
| Acteurs | `GET /acteurs`, `GET /acteurs/page?page=&size=&sort=&direction=`, `GET /acteurs/{id}`, `POST /acteurs`, `PUT /acteurs/{id}`, `DELETE /acteurs/{id}`, `GET /acteurs/{id}/films` |
| Commentaires | `GET /films/{id}/commentaires`, `POST /films/{id}/commentaires`, `GET /commentaires/{id}`, `DELETE /commentaires/{id}` |

Les codes de retour et les corps attendus sont détaillés dans le README racine. Les erreurs
(404 pour une ressource inconnue, 400 pour un commentaire invalide) sont renvoyées au format
`ProblemDetail` (`application/problem+json`) par `web/ApiExceptionHandler`.

## Architecture

| Package | Rôle |
|---|---|
| `model` | entités JPA `Film`, `Acteur`, `Role`, `Commentaire`, énumération `Genre` |
| `repository` | interfaces Spring Data `FilmRepository`, `ActeurRepository`, `RoleRepository`, `CommentaireRepository` |
| `dto` | records échangés avec le client et mappers |
| `service` | règles métier, conversion entité ⇄ DTO, transactions, exceptions métier |
| `web` | contrôleurs REST et `ApiExceptionHandler` |
| `config` | `CorsConfig` : configuration CORS globale |

Les contrôleurs ne manipulent que des DTO : aucune entité JPA n'est exposée par l'API.

Requêtes personnalisées :
- `ActeurRepository.findByRolesFilmId` : acteurs d'un film, **par convention de nommage** ;
- `FilmRepository.findFilmsDeActeur` : films d'un acteur, **avec `@Query`** (JPQL).

## Vérification de CORS

```bash
curl -i -X OPTIONS http://localhost:8080/films \
  -H "Origin: http://localhost:4200" \
  -H "Access-Control-Request-Method: POST"
```

La réponse contient `Access-Control-Allow-Origin: http://localhost:4200`.
