import { Component, DestroyRef, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';

import { ActeurService } from '../acteur-service';
import { Acteur } from '../acteur.model';

@Component({
  selector: 'app-acteur-list',
  imports: [RouterLink],
  templateUrl: './acteur-list.html',
  styleUrl: './acteur-list.css'
})
export class ActeurList implements OnInit {

  private acteurService = inject(ActeurService);
  private destroyRef = inject(DestroyRef);

  acteurs = signal<Acteur[]>([]);
  erreur = signal<string | null>(null);

  ngOnInit() {
    this.acteurService.getAll()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: acteurs => this.acteurs.set(acteurs),
        error: () => this.erreur.set("Impossible de charger les acteurs. Vérifiez que l'API est démarrée.")
      });
  }
}