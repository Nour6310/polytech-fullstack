import { Acteur } from './acteur.model';

export interface ActeurRole extends Acteur {
  personnage: string | null;
}

export interface Film {
  id: number;
  titre: string;
  realisateur: string;
  dateSortie: string;
  genre: string;
  acteurs?: ActeurRole[];
}