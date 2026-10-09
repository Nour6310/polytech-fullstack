import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';

import { ActeurService } from '../acteur-service';
import { Acteur } from '../acteur.model';

type ColonneTri = 'prenom' | 'nom';

@Component({
  selector: 'app-acteur-list',
  imports: [RouterLink],
  templateUrl: './acteur-list.html',
  styleUrl: './acteur-list.css'
})
export class ActeurList implements OnInit {

  private acteurService = inject(ActeurService);
  private destroyRef = inject(DestroyRef);

  private readonly taille = 2;

  acteurs = signal<Acteur[]>([]);
  erreur = signal<string | null>(null);

  page = signal(0);
  totalPages = signal(0);
  tri = signal<ColonneTri>('nom');
  direction = signal<'asc' | 'desc'>('asc');

  estPremiere = computed(() => this.page() === 0);
  estDerniere = computed(() => this.page() >= this.totalPages() - 1);

  ngOnInit() {
    this.charger();
  }

  trierPar(colonne: ColonneTri) {
    if (this.tri() === colonne) {
      this.direction.set(this.direction() === 'asc' ? 'desc' : 'asc');
    } else {
      this.tri.set(colonne);
      this.direction.set('asc');
    }
    this.page.set(0);
    this.charger();
  }

  fleche(colonne: ColonneTri): string {
    if (this.tri() !== colonne) {
      return '';
    }
    return this.direction() === 'asc' ? '▲' : '▼';
  }

  precedent() {
    if (!this.estPremiere()) {
      this.page.update(p => p - 1);
      this.charger();
    }
  }

  suivant() {
    if (!this.estDerniere()) {
      this.page.update(p => p + 1);
      this.charger();
    }
  }

  private charger() {
    this.acteurService.getPage(this.page(), this.taille, this.tri(), this.direction())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: resultat => {
          this.erreur.set(null);
          this.acteurs.set(resultat.contenu);
          this.totalPages.set(resultat.totalPages);
        },
        error: () => this.erreur.set("Impossible de charger les acteurs. Vérifiez que l'API est démarrée.")
      });
  }
}
