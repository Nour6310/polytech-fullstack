import { Component, DestroyRef, OnInit, computed, inject, input, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { FilmService } from '../film-service';
import { ActeurService } from '../acteur-service';
import { Film } from '../film.model';
import { Acteur } from '../acteur.model';

@Component({
  selector: 'app-film-detail',
  imports: [DatePipe, RouterLink, FormsModule],
  templateUrl: './film-detail.html',
  styleUrl: './film-detail.css'
})
export class FilmDetail implements OnInit {

  id = input.required<string>();

  private filmService = inject(FilmService);
  private acteurService = inject(ActeurService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  film = signal<Film | null>(null);
  erreur = signal<string | null>(null);

  tousLesActeurs = signal<Acteur[]>([]);

  acteurSelectionne = signal<number | null>(null);

  acteursDisponibles = computed(() => {
    const dejaDansLeFilm = this.film()?.acteurs ?? [];
    return this.tousLesActeurs().filter(a => !dejaDansLeFilm.some(d => d.id === a.id));
  });

  ngOnInit() {
    this.charger();
    this.acteurService.getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: acteurs => this.tousLesActeurs.set(acteurs),
        error: () => this.erreur.set('Impossible de charger la liste des acteurs.')
      });
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

  associer() {
    const film = this.film();
    const acteurId = this.acteurSelectionne();
    if (!film || !acteurId) {
      return;
    }
    this.filmService.associerActeur(film.id, acteurId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.acteurSelectionne.set(null);
          this.charger();
        },
        error: () => this.erreur.set('Association impossible.')
      });
  }

  dissocier(acteur: Acteur) {
    const film = this.film();
    if (!film) {
      return;
    }
    this.filmService.dissocierActeur(film.id, acteur.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => this.charger(),
        error: () => this.erreur.set('Dissociation impossible.')
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