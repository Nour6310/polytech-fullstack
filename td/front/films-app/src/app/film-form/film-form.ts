import { Component, DestroyRef, OnInit, computed, inject, input, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { FilmService } from '../film-service';

@Component({
  selector: 'app-film-form',
  imports: [FormsModule, RouterLink],
  templateUrl: './film-form.html',
  styleUrl: './film-form.css'
})
export class FilmForm implements OnInit {

  id = input<string>();

  private filmService = inject(FilmService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  // les valeurs de ton enum Genre côté Java
  genres = ['Action', 'Aventure', 'Comedie', 'Drame', 'Fantastique', 'Horreur', 'Policier', 'ScienceFiction'];

  titre = signal('');
  realisateur = signal('');
  dateSortie = signal('');
  genre = signal('');

  erreur = signal<string | null>(null);

  edition = computed(() => this.id() !== undefined);

  ngOnInit() {
    const id = this.id();
    if (id) {
      this.filmService.getById(Number(id))
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: film => {
            this.titre.set(film.titre);
            this.realisateur.set(film.realisateur);
            this.dateSortie.set(film.dateSortie);
            this.genre.set(film.genre);
          },
          error: () => this.erreur.set('Impossible de charger le film.')
        });
    }
  }

  enregistrer() {
    const film = {
      titre: this.titre(),
      realisateur: this.realisateur(),
      dateSortie: this.dateSortie(),
      genre: this.genre()
    };

    const id = this.id();
    const requete = id
      ? this.filmService.modifier(Number(id), film)   
      : this.filmService.creer(film);                 

    requete
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: filmEnregistre => this.router.navigate(['/films', filmEnregistre.id]),
        error: () => this.erreur.set("Enregistrement impossible. Vérifiez les champs et que l'API est démarrée.")
      });
  }
}