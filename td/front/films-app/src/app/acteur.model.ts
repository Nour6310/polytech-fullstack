export interface Acteur {
  id: number;
  prenom: string;
  nom: string;
}

export interface Page<T> {
  contenu: T[];
  page: number;
  taille: number;
  totalElements: number;
  totalPages: number;
}
