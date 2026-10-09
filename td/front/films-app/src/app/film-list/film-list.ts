import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';

import { FilmService } from '../film-service';
import { Film } from '../film.model';
import { FilmCard } from '../film-card/film-card';

@Component({
  selector: 'app-film-list',
  imports: [RouterLink, FilmCard],
  templateUrl: './film-list.html',
  styleUrl: './film-list.css'
})
export class FilmList implements OnInit {

  private filmService = inject(FilmService);
  private destroyRef = inject(DestroyRef);

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

  onSupprimer(film: Film) {
    if (!confirm(`Supprimer « ${film.titre} » ?`)) {
      return;
    }
    this.filmService.supprimer(film.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => this.films.update(liste => liste.filter(f => f.id !== film.id)),
        error: () => this.erreur.set('Suppression impossible.')
      });
  }
}