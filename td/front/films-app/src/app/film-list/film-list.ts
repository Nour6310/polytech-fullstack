import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { FilmService } from '../film-service';
import { Film } from '../film.model';

@Component({
  selector: 'app-film-list',
  imports: [DatePipe, RouterLink],
  templateUrl: './film-list.html',
  styleUrl: './film-list.css'
})
export class FilmList implements OnInit {

  private filmService = inject(FilmService);
  private destroyRef = inject(DestroyRef);

  // l'état de l'écran
  films = signal<Film[]>([]);
  erreur = signal<string | null>(null);
  ngOnInit() {
    this.charger();
  }

  charger() {
    this.filmService.getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: films => {
          this.films.set(films);
          this.erreur.set(null);
        },
        error: () => this.erreur.set("Impossible de charger les films. Vérifiez que l'API est démarrée.")
      });
  }
  estAncien(film: Film): boolean {
    return new Date(film.dateSortie).getFullYear() < 2000;
  }
}