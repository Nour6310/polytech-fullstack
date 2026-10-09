import { Component, DestroyRef, OnInit, inject, input, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

import { ActeurService } from '../acteur-service';
import { Acteur } from '../acteur.model';
import { Film } from '../film.model';

@Component({
  selector: 'app-acteur-detail',
  imports: [DatePipe, RouterLink],
  templateUrl: './acteur-detail.html',
  styleUrl: './acteur-detail.css'
})
export class ActeurDetail implements OnInit {

  id = input.required<string>();

  private acteurService = inject(ActeurService);
  private destroyRef = inject(DestroyRef);

  acteur = signal<Acteur | null>(null);
  films = signal<Film[]>([]);
  erreur = signal<string | null>(null);

  ngOnInit() {
    const id = Number(this.id());

    this.acteurService.getById(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: acteur => this.acteur.set(acteur),
        error: e => this.erreur.set(
          e.status === 404
            ? 'Acteur introuvable.'
            : "Impossible de charger l'acteur. Vérifiez que l'API est démarrée."
        )
      });

    this.acteurService.getFilms(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: films => this.films.set(films),
        error: () => this.films.set([])
      });
  }
}