import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Acteur } from './acteur.model';
import { Film } from './film.model';

@Injectable({ providedIn: 'root' })
export class ActeurService {

  private http = inject(HttpClient);
  private url = '/api/acteurs';

  getAll(): Observable<Acteur[]> {
    return this.http.get<Acteur[]>(this.url);
  }

  getById(id: number): Observable<Acteur> {
    return this.http.get<Acteur>(`${this.url}/${id}`);
  }

  getFilms(id: number): Observable<Film[]> {
    return this.http.get<Film[]>(`${this.url}/${id}/films`);
  }
}