import { Routes } from '@angular/router';

import { FilmList } from './film-list/film-list';

export const routes: Routes = [
  { path: 'films', component: FilmList },
  { path: '', redirectTo: 'films', pathMatch: 'full' }
];