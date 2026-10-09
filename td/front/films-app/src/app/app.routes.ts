import { Routes } from '@angular/router';

import { FilmList } from './film-list/film-list';
import { FilmDetail } from './film-detail/film-detail';

export const routes: Routes = [
  { path: 'films', component: FilmList },
  { path: 'films/:id', component: FilmDetail },
  { path: '', redirectTo: 'films', pathMatch: 'full' }
];