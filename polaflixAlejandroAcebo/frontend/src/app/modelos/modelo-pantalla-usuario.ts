export interface SeriePersonalView {
  idSerie: number;
  nombreSerie: string;
}

export interface UsuarioHomeView {
  empezadas: SeriePersonalView[];
  pendientes: SeriePersonalView[];
  terminadas: SeriePersonalView[];
}

export interface SerieCatalogoView {
  idSerie: number;
  nombreSerie: string;
  sinopsis: string;
  creadores: string[];
  actores: string[];
  agregada: boolean;
}

export interface CatalogoView {
  series: SerieCatalogoView[];
}

export interface CapituloDetalleView {
  idCapitulo: number;
  nombreCapitulo: string;
  numeroCapitulo: number;
  descripcion: string;
  visto: boolean;
}

export interface TemporadaDetalleView {
  nombreTemporada: string;
  capitulos: CapituloDetalleView[];
}

export interface SerieDetalleView {
  serie: {
    nombreSerie: string;
  };
  temporadas: TemporadaDetalleView[];
  temporadaInicial: number;
}

export interface CargoFacturaView {
  fecha: string;
  nombreSerie: string;
  episodio: string;
  precio: number;
}

export interface FacturaMensualView {
  cargos: CargoFacturaView[];
  total: number;
}
