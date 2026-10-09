import { Component, input, output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { Film } from '../film.model';

@Component({
  selector: 'app-film-card',
  imports: [DatePipe, RouterLink],
  templateUrl: './film-card.html',
  styleUrl: './film-card.css'
})
export class FilmCard {

  film = input.required<Film>();

  supprimer = output<Film>();

  estAncien(): boolean {
    return new Date(this.film().dateSortie).getFullYear() < 2000;
  }

  onSupprimer() {
    this.supprimer.emit(this.film());
  }
}