export interface Capitulo {
  idCapitulo: number;
  idTemporada: number;
  nombreCapitulo: string;
  numeroCapitulo: number;
  enlace: string;
  descripcion: string;
}

export interface CapituloConEstado extends Capitulo {
  visto: boolean;
}
