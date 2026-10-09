# Bibliothèque de films — projet fullstack

**Autrice :** Nour El Houda Ben Hassine — nour-el-houda.ben-hassine7@etu.univ-lorraine.fr

Ce dépôt contient mon projet du cours de développement fullstack (Polytech) : une application
pour gérer une bibliothèque de **films** et d'**acteurs**. On peut lister, créer, modifier et
supprimer des films, associer des acteurs à un film en précisant le personnage qu'ils jouent,
et laisser des commentaires sur un film.

L'application est découpée en deux parties qui tournent séparément :

- un **back** : une API REST écrite en Java avec Spring Boot, qui stocke les données dans PostgreSQL ;
- un **front** : une application Angular qui appelle cette API et affiche les écrans.

## Sommaire

1. [Organisation du dépôt](#1-organisation-du-dépôt)
2. [Ce qu'il faut installer](#2-ce-quil-faut-installer)
3. [Créer la base PostgreSQL](#3-créer-la-base-postgresql)
4. [Lancer l'application](#4-lancer-lapplication)
5. [Comment le front et le back communiquent](#5-comment-le-front-et-le-back-communiquent)
6. [Ce que fait l'application](#6-ce-que-fait-lapplication)
7. [Les endpoints de l'API](#7-les-endpoints-de-lapi)
8. [Organisation du code](#8-organisation-du-code)
9. [Ce que j'ai utilisé en plus du cours](#9-ce-que-jai-utilisé-en-plus-du-cours)
10. [Tester l'API avec les fichiers .http](#10-tester-lapi-avec-les-fichiers-http)
11. [Les tags](#11-les-tags)

---

## 1. Organisation du dépôt

```
td/
  back/            API REST Spring Boot (TD 1, TD 2 et bonus du TD 3)
    http/          requêtes de test, à lancer avec l'extension REST Client
  front/
    films-app/     application Angular (TD 3)
tp/                exercices des TP faits pendant le cours (ne font pas partie du rendu)
```

Le fichier [td/back/README.md](td/back/README.md) contient un résumé centré sur l'API.

## 2. Ce qu'il faut installer

| Outil | Version utilisée | Où je l'ai vérifiée |
|---|---|---|
| JDK | 26 | `td/back/build.gradle` (`JavaLanguageVersion.of(26)`) |
| Gradle | 9.7.1, **rien à installer** : le wrapper `./gradlew` le télécharge | `td/back/gradle/wrapper/gradle-wrapper.properties` |
| Spring Boot | 4.1.1 | `td/back/build.gradle` |
| PostgreSQL | une version récente | driver `org.postgresql:postgresql` dans `build.gradle` |
| Node.js | 24 LTS (j'ai la 24.21.0) — Angular 22 demande Node ^22.22.3, ^24.15 ou ^26 | `node -v` |
| npm | fourni avec Node (le projet indique `npm@11.19.0`) | `td/front/films-app/package.json` |
| Angular | 22.2 | `td/front/films-app/package.json` |

Pour tester l'API sans le front : l'extension VS Code **REST Client** (IntelliJ lit les fichiers
`.http` sans extension).

## 3. Créer la base PostgreSQL

La connexion est décrite dans `td/back/src/main/resources/application.properties` :

- base : `films-db` (`jdbc:postgresql://localhost:5432/films-db`) ;
- utilisateur : `postgres` ;
- mot de passe : celui de **votre** installation. Il faut le mettre à jour dans
  `spring.datasource.password` avant de lancer le back.

Créer la base (ou depuis pgAdmin) :

```bash
sudo -u postgres createdb films-db
```

Les tables n'ont pas à être créées à la main : Hibernate les crée au premier démarrage.

### Repartir d'une base propre

Si la base vient d'une version précédente du projet, elle peut contenir des tables qui ne
correspondent plus au code (par exemple l'ancienne table de liaison `film_acteur`, remplacée
par `role`). Le plus simple est de tout supprimer, le back recréera tout au démarrage suivant :

```bash
psql -U postgres -d films-db -c "DROP TABLE IF EXISTS commentaire, role, film_acteur, film, acteur CASCADE;"
```

Au redémarrage, les tables sont recréées et `data.sql` remet les données d'exemple
(3 films, 3 acteurs, 3 rôles).

## 4. Lancer l'application

Il faut deux terminaux : un pour le back, un pour le front.

### Le back

```bash
cd td/back
./gradlew bootRun          # Windows : gradlew.bat bootRun
```

L'API est prête quand la console affiche `Started BackApplication`. Elle écoute sur
**http://localhost:8080** (le port par défaut de Spring Boot, je ne l'ai pas changé).
Vérification rapide : http://localhost:8080/films renvoie une liste de films en JSON.

### Le front

```bash
cd td/front/films-app
npm install                # la première fois seulement
npx ng serve               # ou : npm start
```

Puis ouvrir **http://localhost:4200**. L'adresse `/` redirige vers la liste des films.

## 5. Comment le front et le back communiquent

Le front tourne sur le port 4200 et l'API sur le port 8080. Pour le navigateur, ce sont deux
**origines** différentes : sans configuration, il bloquerait les appels (politique CORS).
J'ai mis en place les deux solutions vues en cours.

### Côté front : le proxy (en développement)

Les services Angular appellent des URL relatives qui commencent par `/api`
(par exemple `/api/films`). Le serveur de développement d'Angular relaie ces appels vers le
back grâce à `td/front/films-app/src/proxy.conf.json`, déclaré dans `angular.json`
(`"proxyConfig": "src/proxy.conf.json"`) :

```json
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "pathRewrite": { "^/api": "" }
  }
}
```

`pathRewrite` retire le préfixe `/api` : `/api/films` arrive donc sur `/films` côté Spring.
Pour le navigateur, tout vient de `localhost:4200`, il n'y a plus de problème d'origine.
Si on modifie `angular.json`, il faut relancer `ng serve`.

### Côté back : CORS

La classe `config/CorsConfig` (un `WebMvcConfigurer`) autorise l'origine définie par la
propriété `app.cors.allowed-origins=http://localhost:4200`, pour les méthodes GET, POST, PUT
et DELETE, sur toutes les URL. C'est ce qui permettrait au front d'appeler l'API directement,
sans proxy. Vérification :

```bash
curl -i -X OPTIONS http://localhost:8080/films \
  -H "Origin: http://localhost:4200" \
  -H "Access-Control-Request-Method: POST"
```

La réponse doit contenir `Access-Control-Allow-Origin: http://localhost:4200`.

## 6. Ce que fait l'application

Un menu en haut de page donne accès aux **Films** et aux **Acteurs** (le lien de la page
courante est mis en évidence).

### Les films

- **Liste** : chaque film s'affiche dans une carte (titre, réalisateur, date au format
  `jj/mm/aaaa`). Les films sortis **avant 2000** sont mis en évidence (bordure et titre colorés).
- **Recherche** : un champ filtre la liste au fur et à mesure de la frappe, sur le titre ou le
  réalisateur. Le filtrage se fait dans le navigateur, sans appel à l'API.
- **Ajout et modification** : un même formulaire sert aux deux (`/films/nouveau` et
  `/films/:id/modifier`). Tous les champs sont obligatoires et le bouton reste désactivé tant que
  le formulaire est incomplet. Le genre se choisit dans une liste.
- **Suppression** : depuis la liste ou depuis le détail, après une confirmation.
- **Détail d'un film** : ses informations, ses acteurs, ses commentaires.

### Les acteurs

- **Liste paginée et triable** : un tableau Prénom / Nom, **2 acteurs par page**, avec des
  boutons Précédent / Suivant (désactivés sur la première et la dernière page) et l'indication
  « Page x / y ». Un clic sur un en-tête trie la colonne, un second clic inverse le sens ; une
  flèche ▲ / ▼ indique le tri en cours.
- **Détail d'un acteur** : la liste des films dans lesquels il joue, avec un lien vers chacun.

### Les liens film ↔ acteur, avec le personnage

Dans le détail d'un film, je peux :

- **associer** un acteur avec un sélecteur, et saisir (facultativement) le **personnage** qu'il
  joue. Le sélecteur ne propose que les acteurs qui ne sont pas déjà dans le film ;
- voir le personnage à côté de chaque acteur (« dans le rôle de … ») ;
- **retirer** un acteur du film.

Côté base, ce lien est une entité `Role` (film, acteur, personnage).

### Les commentaires

Dans le détail d'un film : la liste des commentaires (auteur, date et heure, message), du plus
récent au plus ancien, un petit formulaire auteur + message pour en ajouter un, et un bouton
pour en supprimer un. La date est fixée par le serveur. Supprimer un film supprime aussi ses
commentaires.

### Page introuvable et messages d'erreur

- Une adresse inconnue (par exemple `/nimporte-quoi`) affiche une page « Page introuvable » avec
  un lien de retour vers les films.
- Si l'API ne répond pas, chaque écran affiche un message clair au lieu de rester vide
  (par exemple « Impossible de charger les films. Vérifiez que l'API est démarrée. »).
- Un film ou un acteur inexistant affiche « Film introuvable. » ou « Acteur introuvable. ».
- Les listes vides ont un message (« Aucun film enregistré. », « Aucun commentaire pour ce film. »…).

### Les données sont conservées entre deux redémarrages

- `spring.jpa.hibernate.ddl-auto=update` : Hibernate crée les tables qui manquent mais ne
  supprime jamais les données.
- `data.sql` est exécuté à chaque démarrage (`spring.sql.init.mode=always`, après la création
  des tables grâce à `spring.jpa.defer-datasource-initialization=true`). Chaque insertion est
  protégée par `WHERE NOT EXISTS (SELECT 1 FROM <table>)` : les données d'exemple ne sont ajoutées
  que si la table est vide. Le fichier peut donc être rejoué sans créer de doublons, et ce que
  j'ajoute depuis l'application reste en base.

## 7. Les endpoints de l'API

Les URL ci-dessous sont celles du back (port 8080). Depuis le front, elles sont préfixées par
`/api`. Les identifiants dans les URL n'acceptent que des chiffres (`{id:\d+}`) : `/films/abc`
renvoie donc un 404.

### Films — `FilmController`

| Méthode | URL | Rôle | Codes |
|---|---|---|---|
| GET | `/films` | liste des films (sans acteurs) | 200 |
| GET | `/films/{id}` | détail d'un film, avec ses acteurs et leur personnage | 200, 404 |
| POST | `/films` | crée un film | 201 + `Location` |
| PUT | `/films/{id}` | modifie un film (ses acteurs sont conservés) | 200, 404 |
| DELETE | `/films/{id}` | supprime un film, ses rôles et ses commentaires | 204, 404 |
| GET | `/films/{id}/acteurs` | acteurs d'un film | 200, 404 |
| POST | `/films/{id}/acteurs/{acteurId}` | associe un acteur au film ; corps facultatif `{ "personnage": "..." }` ; renvoie le détail du film | 200, 404 |
| DELETE | `/films/{id}/acteurs/{acteurId}` | retire un acteur du film | 204, 404 |

Corps attendu pour `POST /films` et `PUT /films/{id}` :

```json
{
  "titre": "Blade Runner",
  "realisateur": "Ridley Scott",
  "dateSortie": "1982-06-25",
  "genre": "ScienceFiction"
}
```

Genres possibles : `Action`, `Aventure`, `Comedie`, `Drame`, `Fantastique`, `Horreur`,
`Policier`, `ScienceFiction`.

Si un acteur est déjà dans le film, `POST /films/{id}/acteurs/{acteurId}` ne crée pas de
doublon : il met seulement à jour le personnage s'il est fourni.

### Acteurs — `ActeurController`

| Méthode | URL | Rôle | Codes |
|---|---|---|---|
| GET | `/acteurs` | liste des acteurs (sans leurs films) | 200 |
| GET | `/acteurs/page` | liste paginée et triée (voir ci-dessous) | 200 |
| GET | `/acteurs/{id}` | détail d'un acteur | 200, 404 |
| POST | `/acteurs` | crée un acteur, corps `{ "prenom": "...", "nom": "..." }` | 201 + `Location` |
| PUT | `/acteurs/{id}` | modifie un acteur | 200, 404 |
| DELETE | `/acteurs/{id}` | supprime un acteur (et ses rôles) | 204, 404 |
| GET | `/acteurs/{id}/films` | films d'un acteur | 200, 404 |

Paramètres de `/acteurs/page` : `page` (défaut `0`), `size` (défaut `2`), `sort` (défaut `nom`,
valeurs acceptées `id`, `prenom`, `nom` ; toute autre valeur revient à `nom`) et `direction`
(`asc` par défaut, ou `desc`). Exemple : `/acteurs/page?page=1&sort=prenom&direction=desc`.
La réponse est un objet :

```json
{ "contenu": [ { "id": 1, "prenom": "Leonardo", "nom": "DiCaprio" } ],
  "page": 1, "taille": 2, "totalElements": 3, "totalPages": 2 }
```

### Commentaires — `CommentaireController`

| Méthode | URL | Rôle | Codes |
|---|---|---|---|
| GET | `/films/{id}/commentaires` | commentaires d'un film, du plus récent au plus ancien | 200, 404 |
| POST | `/films/{id}/commentaires` | ajoute un commentaire, corps `{ "auteur": "...", "message": "..." }` | 201 + `Location`, 400, 404 |
| GET | `/commentaires/{id}` | un commentaire (c'est l'adresse renvoyée dans `Location`) | 200, 404 |
| DELETE | `/commentaires/{id}` | supprime un commentaire | 204, 404 |

L'auteur et le message sont obligatoires (100 et 2000 caractères au maximum), sinon la réponse
est un 400.

L'en-tête `Location` des créations contient un chemin relatif, par exemple `/films/4`.

### Format des erreurs : ProblemDetail

Les erreurs gérées par `web/ApiExceptionHandler` (un `@RestControllerAdvice`) sont renvoyées au
format **ProblemDetail** (RFC 9457), avec le type `application/problem+json` :

| Exception | Code |
|---|---|
| `FilmNotFoundException` | 404 |
| `ActeurNotFoundException` | 404 |
| `CommentaireNotFoundException` | 404 |
| `CommentaireInvalideException` (auteur ou message manquant ou trop long) | 400 |

Exemple pour `GET /films/9999` :

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Aucun film trouvé avec l'id 9999",
  "instance": "/films/9999"
}
```

Côté front, ce sont ces codes (404, 400…) qui permettent d'afficher le bon message.

## 8. Organisation du code

### Le back — `td/back/src/main/java/com/exemple/back`

Je respecte la frontière vue en cours : les contrôleurs ne manipulent que des DTO, le service
fait la conversion, le repository ne connaît que les entités.

| Package | Contenu et rôle |
|---|---|
| (racine) | `BackApplication` : la classe `@SpringBootApplication` qui démarre l'application |
| `config` | `CorsConfig` : la configuration CORS globale |
| `model` | les entités JPA `Film`, `Acteur`, `Role` (le personnage joué par un acteur dans un film), `Commentaire`, et l'énumération `Genre` |
| `repository` | les interfaces Spring Data : `FilmRepository`, `ActeurRepository`, `RoleRepository`, `CommentaireRepository` |
| `dto` | les records échangés avec le client (`FilmDto`, `FilmDetailDto`, `FilmCreationDto`, `ActeurDto`, `ActeurCreationDto`, `ActeurRoleDto`, `RoleCreationDto`, `CommentaireDto`, `CommentaireCreationDto`, `PageDto`) et les mappers (`FilmMapper`, `ActeurMapper`, `CommentaireMapper`) |
| `service` | la logique métier et les transactions (`FilmService`, `ActeurService`, `CommentaireService`) et les exceptions métier |
| `web` | les contrôleurs REST (`FilmController`, `ActeurController`, `CommentaireController`) et `ApiExceptionHandler` |

Les relations entre entités :

- `Film` 1 — n `Role` n — 1 `Acteur` : le `@ManyToMany` du TD 2 est devenu une entité `Role`
  avec deux `@ManyToOne` (table `role`, un seul rôle par couple film / acteur) ;
- `Film` 1 — n `Commentaire` (table `commentaire`).

Deux requêtes personnalisées demandées au TD 2 :

- `ActeurRepository.findByRolesFilmId` : les acteurs d'un film, **par convention de nommage** ;
- `FilmRepository.findFilmsDeActeur` : les films d'un acteur, **avec `@Query`** (JPQL).

Ressources (`src/main/resources`) : `application.properties` (configuration) et `data.sql`
(données d'exemple).

### Le front — `td/front/films-app/src/app`

| Élément | Rôle |
|---|---|
| `app.ts`, `app.html` | le composant racine : le menu et le `<router-outlet />` |
| `app.config.ts` | les providers : routeur (avec `withComponentInputBinding()`) et `HttpClient` |
| `app.routes.ts` | la table des routes |
| `film.model.ts`, `acteur.model.ts`, `commentaire.model.ts` | les interfaces TypeScript, calquées sur les DTO (`Film`, `ActeurRole`, `Acteur`, `Page<T>`, `Commentaire`) |
| `film-service.ts` | tous les appels HTTP vers `/api/films` (CRUD, acteurs, commentaires) et `/api/commentaires` |
| `acteur-service.ts` | les appels HTTP vers `/api/acteurs` (liste, page, détail, films d'un acteur) |
| `film-list/` | la liste des films et la recherche |
| `film-card/` | la carte d'un film : reçoit le film par `input()`, signale la demande de suppression par `output()` ; c'est la liste qui appelle l'API |
| `film-detail/` | le détail d'un film : acteurs et personnages, association / dissociation, commentaires, suppression |
| `film-form/` | le formulaire de création et de modification |
| `acteur-list/` | la liste paginée et triable des acteurs |
| `acteur-detail/` | le détail d'un acteur et ses films |
| `not-found/` | la page « Page introuvable » |

Les routes :

| URL | Composant |
|---|---|
| `/films` | `FilmList` |
| `/films/nouveau` | `FilmForm` (déclarée avant `films/:id`) |
| `/films/:id/modifier` | `FilmForm` |
| `/films/:id` | `FilmDetail` |
| `/acteurs` | `ActeurList` |
| `/acteurs/:id` | `ActeurDetail` |
| `''` | redirection vers `/films` |
| `**` | `NotFound` |

Hors de `src/app` : `src/styles.css` (feuille de style globale, avec la palette de couleurs) et
`src/proxy.conf.json` (le proxy).

## 9. Ce que j'ai utilisé en plus du cours

J'ai comparé mon code aux supports du cours (Spring parties 1 et 2, Angular parties 1 et 2).
Voici ce qui n'y figure pas, ou n'y est que cité, et pourquoi je l'ai utilisé.

### Côté back

- **`PageRequest`, `Sort` et `Page` de Spring Data.** Le cours cite la pagination
  (`findAll(Pageable)`, `Sort`) comme piste « pour aller plus loin », sans la détailler.
  `PageRequest.of(page, taille, Sort.by(sens, colonne))` décrit la page voulue et le tri ;
  `findAll` renvoie un `Page<Acteur>` qui contient la page et les totaux. Je construis
  `PageRequest` dans le service à partir de paramètres simples, pour pouvoir refuser les colonnes
  de tri non prévues (seuls `id`, `prenom` et `nom` sont acceptés).
- **Un record générique `PageDto<T>`.** Le `<T>` permet d'écrire une seule classe « une page de
  quelque chose ». Je renvoie ce DTO plutôt que le `Page` de Spring pour garder la frontière
  entité / DTO et un JSON simple et stable.
- **Une contrainte d'unicité sur deux colonnes** :
  `@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"film_id", "acteur_id"}))` sur
  `Role`. Le cours montre `unique = true` sur une seule colonne. Ici, c'est le couple film / acteur
  qui doit être unique : un acteur ne peut pas être associé deux fois au même film.
- **Des requêtes par convention de nommage qui traversent une relation**
  (`findByRolesFilmId`, `findByFilmIdAndActeurId`, `findByFilmIdOrderByDateCreationDescIdDesc`).
  Le cours montre la convention sur des attributs simples. Spring sait aussi suivre les
  relations : `RolesFilmId` signifie « l'id du film des rôles de l'acteur ». Ça m'évite d'écrire
  du JPQL.
- **`@RequestBody(required = false)`.** Par défaut, Spring exige un corps de requête. Pour
  l'association film / acteur, le personnage est facultatif : on peut appeler l'URL sans corps,
  comme au TD 2.
- **`URI.create("/films/" + id)` pour l'en-tête `Location`.** Le cours utilise
  `ServletUriComponentsBuilder`, qui produit une URL complète. J'ai choisi un chemin relatif,
  plus simple à lire, que le client complète avec l'adresse du serveur.
- **`ddl-auto=update` et un `data.sql` rejouable avec `INSERT … SELECT … WHERE NOT EXISTS`.**
  Le cours recommande `create-drop`, qui vide la base à chaque arrêt, et présente `update` comme
  « à éviter ». Je l'ai choisi en connaissance de cause pour le TD 3 : il fallait que les données
  survivent aux redémarrages. La clause `WHERE NOT EXISTS` (du SQL standard) empêche `data.sql`
  de réinsérer les données d'exemple à chaque démarrage. La contrepartie est qu'`update` ne
  supprime jamais une ancienne table : c'est pour ça que j'explique comment vider la base
  (section 3).
- **Quelques outils standard de Java** : `Set.of(...)` pour la liste fixe des colonnes de tri,
  `List.copyOf(...)` pour parcourir une copie d'une collection pendant que je la modifie, et
  `Comparator.comparing(...)` pour trier les acteurs d'un film par nom puis prénom.

### Côté front

- **`takeUntilDestroyed` et `DestroyRef`.** Le cours demande de ne laisser aucun `subscribe()`
  sans désabonnement et propose le pipe `async` ou `toSignal`. J'ai gardé des `subscribe()`,
  parce que j'avais besoin du callback `error` pour afficher un message et que plusieurs écrans
  rechargent les données après une action. Chaque appel passe par
  `.pipe(takeUntilDestroyed(this.destroyRef))` : l'abonnement est annulé automatiquement quand le
  composant est détruit, sans fuite de mémoire.
- **`HttpParams`.** Pour la pagination, `ActeurService.getPage` construit les paramètres
  `?page=…&size=…&sort=…&direction=…` avec `HttpParams`, plutôt que de les concaténer à la main
  dans l'URL.
- **Les types littéraux de TypeScript** : `type ColonneTri = 'prenom' | 'nom'` et
  `'asc' | 'desc'`. Le compilateur refuse toute autre valeur, ce qui évite de demander un tri sur
  une colonne qui n'existe pas.
- **`confirm()`**, la boîte de dialogue native du navigateur, pour demander une confirmation
  avant de supprimer un film ou un commentaire.
- **Les variables CSS** (`--primaire`, `--danger`, `--ancien`… dans `:root`, utilisées avec
  `var(--…)`). Elles définissent la palette une seule fois dans `styles.css`, et tous les
  composants la réutilisent. J'ai aussi utilisé `white-space: pre-line` pour conserver les retours
  à la ligne des commentaires.
- **`provideBrowserGlobalErrorListeners()`** dans `app.config.ts` : ajouté automatiquement par
  `ng new`, il fait remonter dans la console les erreurs non interceptées. Je l'ai laissé tel quel.

## 10. Tester l'API avec les fichiers .http

Les requêtes de test sont dans `td/back/http/`, un fichier par ressource, séparées par `###` :

- `films.http` : CRUD des films, erreurs 404, association / dissociation des acteurs, acteurs
  d'un film, commentaires (ajout, lecture, erreurs 400 et 404, suppression) ;
- `acteurs.http` : CRUD des acteurs, erreurs 404, films d'un acteur.

Pour les lancer :

1. installer l'extension **REST Client** dans VS Code ;
2. démarrer le back ;
3. ouvrir un fichier `.http` et cliquer sur **Send Request** au-dessus d'une requête.

Le résultat attendu est écrit en commentaire au-dessus de chaque requête. Les requêtes utilisent
des identifiants précis (par exemple le film 1 ou l'acteur 4) : comme la base n'est plus vidée au
démarrage, il vaut mieux partir d'une base propre (section 3) pour obtenir exactement les résultats
annoncés.

## 11. Les tags

| Tag | Contenu |
|---|---|
| `td1` | API REST des films, stockage en mémoire (une `Map` dans le repository), erreurs au format ProblemDetail, `films.http` |
| `td2` | persistance PostgreSQL avec Spring Data JPA, DTO et mappers, acteurs et relation `ManyToMany` avec les films, requêtes par convention de nommage et `@Query`, CORS, `acteurs.http` |
| `td3` | front Angular complet branché sur l'API, puis les bonus : recherche, pagination et tri des acteurs, rôles (personnage), commentaires, données conservées entre deux redémarrages |
