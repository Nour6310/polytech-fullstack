import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Film } from './film.model';
import { Acteur } from './acteur.model';
import { Commentaire } from './commentaire.model';

@Injectable({ providedIn: 'root' })
export class FilmService {

  private http = inject(HttpClient);
  private url = '/api/films';

  getAll(): Observable<Film[]> {
    return this.http.get<Film[]>(this.url);
  }

  getById(id: number): Observable<Film> {
    return this.http.get<Film>(`${this.url}/${id}`);
  }

  getActeurs(id: number): Observable<Acteur[]> {
    return this.http.get<Acteur[]>(`${this.url}/${id}/acteurs`);
  }

  creer(film: Partial<Film>): Observable<Film> {
    return this.http.post<Film>(this.url, film);
  }

  modifier(id: number, film: Partial<Film>): Observable<Film> {
    return this.http.put<Film>(`${this.url}/${id}`, film);
  }

  supprimer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  associerActeur(filmId: number, acteurId: number, personnage: string | null = null): Observable<Film> {
    return this.http.post<Film>(`${this.url}/${filmId}/acteurs/${acteurId}`, { personnage });
  }

  dissocierActeur(filmId: number, acteurId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${filmId}/acteurs/${acteurId}`);
  }

  getCommentaires(filmId: number): Observable<Commentaire[]> {
    return this.http.get<Commentaire[]>(`${this.url}/${filmId}/commentaires`);
  }

  ajouterCommentaire(filmId: number, auteur: string, message: string): Observable<Commentaire> {
    return this.http.post<Commentaire>(`${this.url}/${filmId}/commentaires`, { auteur, message });
  }

  supprimerCommentaire(id: number): Observable<void> {
    return this.http.delete<void>(`/api/commentaires/${id}`);
  }
}