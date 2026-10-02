# Bibliothèque de films — API REST (TD 1 et TD 2)

API REST développée avec Spring Boot pour gérer une bibliothèque de films et leurs acteurs.

- **TD 1** : API REST, stockage en mémoire, erreurs au format `ProblemDetail`.
- **TD 2** : persistance dans PostgreSQL avec Spring Data JPA, séparation entités / DTO,
  relation `ManyToMany` entre films et acteurs, configuration CORS.

## Prérequis

- JDK 26
- PostgreSQL (testé avec la version 16)
- Gradle n'est pas à installer : le wrapper `./gradlew` est fourni.

## Préparer la base de données

1. Créer la base `films-db` :
```bash
   sudo -u postgres createdb films-db
```
2. Donner à l'utilisateur `postgres` le mot de passe utilisé dans `application.properties` :
```bash
   sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'votre_mot_de_passe';"
```

On peut aussi créer la base depuis pgAdmin.

## Configuration

Tout se trouve dans `src/main/resources/application.properties` :

| Propriété | Rôle |
|---|---|
| `spring.datasource.url` | adresse de la base : `jdbc:postgresql://localhost:5432/films-db` |
| `spring.datasource.username` / `password` | identifiants PostgreSQL, à adapter à votre installation |
| `spring.jpa.hibernate.ddl-auto=create-drop` | Hibernate crée les tables au démarrage et les supprime à l'arrêt |
| `spring.jpa.show-sql=true` | affiche dans la console chaque requête SQL exécutée |
| `spring.jpa.open-in-view=false` | la session Hibernate se ferme à la fin de la transaction |
| `spring.sql.init.mode=always` | exécute `data.sql` au démarrage |
| `spring.jpa.defer-datasource-initialization=true` | exécute `data.sql` après la création des tables |
| `app.cors.allowed-origins` | origine autorisée à appeler l'API depuis un navigateur (`http://localhost:4200`) |

Le fichier `src/main/resources/data.sql` insère au démarrage 3 films, 3 acteurs
et quelques associations entre eux.

## Démarrer l'application

Depuis le dossier `td/back` :

```bash
./gradlew bootRun        # Windows : gradlew.bat bootRun
```

L'application est prête quand la console affiche `Started BackApplication`.
Elle écoute sur `http://localhost:8080`. Pour l'arrêter : `Ctrl+C`.

## Tester l'API

Les requêtes de test sont dans le dossier `http/`, un fichier par ressource :

- `http/films.http` : films, association et dissociation des acteurs ;
- `http/acteurs.http` : acteurs.

1. Installer l'extension **REST Client** dans VS Code (IntelliJ exécute les fichiers `.http` nativement).
2. Démarrer l'application.
3. Ouvrir un fichier `.http` et cliquer sur **Send Request** au-dessus de chaque requête.

Lancer les requêtes **dans l'ordre**, à partir d'une application **fraîchement démarrée** :
avec `create-drop`, la base repart des données de `data.sql` à chaque démarrage,
et les identifiants supprimés ne sont jamais réutilisés.

## Endpoints

### Films

| Verbe | URL | Description | Codes |
|---|---|---|---|
| GET | `/films` | liste des films (sans les acteurs) | 200 |
| GET | `/films/{id}` | détail d'un film, avec ses acteurs | 200, 404 |
| POST | `/films` | création d'un film | 201 + Location |
| PUT | `/films/{id}` | modification d'un film (ses acteurs sont conservés) | 200, 404 |
| DELETE | `/films/{id}` | suppression d'un film | 204, 404 |
| GET | `/films/{id}/acteurs` | acteurs d'un film | 200, 404 |
| POST | `/films/{id}/acteurs/{acteurId}` | associe un acteur à un film | 200, 404 |
| DELETE | `/films/{id}/acteurs/{acteurId}` | dissocie un acteur d'un film | 204, 404 |

Exemple de corps pour `POST` et `PUT` :

```json
{
  "titre": "Blade Runner",
  "realisateur": "Ridley Scott",
  "dateSortie": "1982-06-25",
  "genre": "ScienceFiction"
}
```

Genres disponibles : `Action`, `Aventure`, `Comedie`, `Drame`, `Fantastique`, `Horreur`, `Policier`, `ScienceFiction`.

### Acteurs

| Verbe | URL | Description | Codes |
|---|---|---|---|
| GET | `/acteurs` | liste des acteurs (sans leurs films) | 200 |
| GET | `/acteurs/{id}` | détail d'un acteur | 200, 404 |
| POST | `/acteurs` | création d'un acteur | 201 + Location |
| PUT | `/acteurs/{id}` | modification d'un acteur | 200, 404 |
| DELETE | `/acteurs/{id}` | suppression d'un acteur (il est d'abord retiré de ses films) | 204, 404 |
| GET | `/acteurs/{id}/films` | films d'un acteur | 200, 404 |

Exemple de corps : `{ "prenom": "Tom", "nom": "Hardy" }`

### Erreurs

Un film ou un acteur inconnu renvoie une erreur **404** au format `ProblemDetail`
(`application/problem+json`), par exemple :

```json
{
  "title": "Not Found",
  "status": 404,
  "detail": "Aucun film trouvé avec l'id 9999",
  "instance": "/films/9999"
}
```

## Architecture

| Package | Rôle |
|---|---|
| `model` | entités JPA `Film` et `Acteur` (relation `ManyToMany`, table `film_acteur`), énumération `Genre` |
| `repository` | interfaces Spring Data `FilmRepository` et `ActeurRepository` |
| `dto` | records échangés avec le client (`FilmDto`, `FilmDetailDto`, `FilmCreationDto`, `ActeurDto`, `ActeurCreationDto`) et mappers |
| `service` | règles métier, conversion entité ⇄ DTO, transactions, exceptions `FilmNotFoundException` et `ActeurNotFoundException` |
| `web` | contrôleurs REST et `ApiExceptionHandler` (erreurs → `ProblemDetail`) |
| `config` | `CorsConfig` : configuration CORS globale |

Les contrôleurs ne manipulent que des DTO : aucune entité JPA n'est exposée par l'API.

Requêtes personnalisées :
- `ActeurRepository.findByFilmsId` : acteurs d'un film, **par convention de nommage** ;
- `FilmRepository.findFilmsDeActeur` : films d'un acteur, **avec `@Query`** (JPQL).

## Vérification de la persistance

Pour vérifier que les données survivent à un redémarrage, passer temporairement à :

```properties
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=never
```

Créer un film, arrêter l'application, vérifier sa présence dans la base
(`select * from film;` dans pgAdmin ou `psql`), redémarrer : le film est toujours renvoyé par `GET /films`.
Remettre ensuite `create-drop` et `always`.

## Vérification de CORS

```bash
curl -i -X OPTIONS http://localhost:8080/films \
  -H "Origin: http://localhost:4200" \
  -H "Access-Control-Request-Method: POST"
```

La réponse contient `Access-Control-Allow-Origin: http://localhost:4200`.