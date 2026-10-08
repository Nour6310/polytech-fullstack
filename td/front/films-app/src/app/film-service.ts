import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Film } from './film.model';
import { Acteur } from './acteur.model';

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

  associerActeur(filmId: number, acteurId: number): Observable<Film> {
    return this.http.post<Film>(`${this.url}/${filmId}/acteurs/${acteurId}`, null);
  }

  dissocierActeur(filmId: number, acteurId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${filmId}/acteurs/${acteurId}`);
  }
}