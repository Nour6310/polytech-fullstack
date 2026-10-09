import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Acteur, Page } from './acteur.model';
import { Film } from './film.model';

@Injectable({ providedIn: 'root' })
export class ActeurService {

  private http = inject(HttpClient);
  private url = '/api/acteurs';

  getAll(): Observable<Acteur[]> {
    return this.http.get<Acteur[]>(this.url);
  }

  getPage(page: number, taille: number, tri: string, direction: 'asc' | 'desc'): Observable<Page<Acteur>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', taille)
      .set('sort', tri)
      .set('direction', direction);
    return this.http.get<Page<Acteur>>(`${this.url}/page`, { params });
  }

  getById(id: number): Observable<Acteur> {
    return this.http.get<Acteur>(`${this.url}/${id}`);
  }

  getFilms(id: number): Observable<Film[]> {
    return this.http.get<Film[]>(`${this.url}/${id}/films`);
  }
}