export interface SeriePersonalView {
  idSerie: number;
  nombreSerie: string;
}

export interface UsuarioHomeView {
  usuario: {
    nombre: string;
  };
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
  inicial: string;
  series: SerieCatalogoView[];
}

export interface CapituloDetalleView {
  idCapitulo: number;
  nombreCapitulo: string;
  numeroCapitulo: number;
  enlace: string;
  descripcion: string;
  visto: boolean;
}

export interface TemporadaDetalleView {
  idTemporada: number;
  nombreTemporada: string;
  numeroTemporada: number;
  capitulos: CapituloDetalleView[];
}

export interface SerieDetalleView {
  serie: {
    nombreSerie: string;
    tipoSerie: string;
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
  anio: number;
  mes: number;
  cuotaFija: boolean;
  cargos: CargoFacturaView[];
  total: number;
}
