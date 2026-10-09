import { Component, DestroyRef, OnInit, inject, input, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

import { FilmService } from '../film-service';
import { Film } from '../film.model';

@Component({
  selector: 'app-film-detail',
  imports: [DatePipe, RouterLink],
  templateUrl: './film-detail.html',
  styleUrl: './film-detail.css'
})
export class FilmDetail implements OnInit {
  id = input.required<string>();

  private filmService = inject(FilmService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  film = signal<Film | null>(null);
  erreur = signal<string | null>(null);

  ngOnInit() {
    this.charger();
  }

  charger() {
    this.filmService.getById(Number(this.id()))
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: film => {
          this.film.set(film);
          this.erreur.set(null);
        },
        error: e => this.erreur.set(
          e.status === 404
            ? 'Film introuvable.'
            : "Impossible de charger le film. Vérifiez que l'API est démarrée."
        )
      });
  }

  supprimer() {
    const film = this.film();
    if (!film || !confirm(`Supprimer « ${film.titre} » ?`)) {
      return;
    }
    this.filmService.supprimer(film.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => this.router.navigate(['/films']),
        error: () => this.erreur.set('Suppression impossible.')
      });
  }
}