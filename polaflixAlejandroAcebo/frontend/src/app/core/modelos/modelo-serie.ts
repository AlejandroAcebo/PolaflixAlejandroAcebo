export interface Serie {
  idSerie: number;
  nombreSerie: string;
  sinopsis: string;
  tipoSerie: 'GOLD' | 'SILVER' | 'ESTANDAR' | string;
}
