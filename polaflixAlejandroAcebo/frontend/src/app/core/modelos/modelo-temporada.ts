import { CapituloConEstado } from './modelo-capitulo';

export interface Temporada {
  idTemporada: number;
  idSerie: number;
  nombreTemporada: string;
  numeroTemporada: number;
}

export interface TemporadaConCapitulos extends Temporada {
  capitulos: CapituloConEstado[];
}
