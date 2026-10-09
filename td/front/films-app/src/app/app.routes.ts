import { Routes } from '@angular/router';

import { FilmList } from './film-list/film-list';
import { FilmDetail } from './film-detail/film-detail';
import { FilmForm } from './film-form/film-form';

export const routes: Routes = [
  { path: 'films', component: FilmList },
  { path: 'films/nouveau', component: FilmForm },       
  { path: 'films/:id/modifier', component: FilmForm },
  { path: 'films/:id', component: FilmDetail },
  { path: '', redirectTo: 'films', pathMatch: 'full' }
];