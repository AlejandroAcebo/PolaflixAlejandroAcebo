export type EstadoSerie = 'EMPEZADA' | 'PENDIENTE' | 'TERMINADA' | string;

export interface SeguimientoSerieRequest {
  idSerie: number;
}
