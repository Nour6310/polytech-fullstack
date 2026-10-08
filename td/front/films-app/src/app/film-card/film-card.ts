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

  // ENTRÉE : le film à afficher, donné par le parent (obligatoire)
  film = input.required<Film>();

  // SORTIE : événement envoyé au parent quand on clique sur « Supprimer »
  supprimer = output<Film>();

  estAncien(): boolean {
    return new Date(this.film().dateSortie).getFullYear() < 2000;
  }

  onSupprimer() {
    this.supprimer.emit(this.film());
  }
}