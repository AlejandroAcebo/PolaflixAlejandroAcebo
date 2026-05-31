import { CapituloConEstado } from './modelo-capitulo';
import { Serie } from './modelo-serie';
import { TemporadaConCapitulos } from './modelo-temporada';
import { Usuario } from './modelo-usuario';

export interface SeguimientoResumen {
  idSeguimientoSerie: number | null;
  estadoSerie: string;
  idCapituloUltimoVisto: number | null;
}

export interface SeriePersonalView extends Serie {
  creadores: string[];
  actores: string[];
  seguimiento: SeguimientoResumen;
  totalCapitulos: number;
  capitulosVistos: number;
}

export interface UsuarioHomeView {
  usuario: Pick<Usuario, 'idUsuario' | 'nombre' | 'idPlan' | 'cargos'>;
  empezadas: SeriePersonalView[];
  pendientes: SeriePersonalView[];
  terminadas: SeriePersonalView[];
}

export interface SerieDetalleView {
  serie: Serie;
  seguimiento: SeguimientoResumen;
  temporadas: TemporadaConCapitulos[];
  vistos: number;
  totalCapitulos: number;
  temporadaInicial: number;
}

export interface SerieCatalogoView extends Serie {
  creadores: string[];
  actores: string[];
  agregada: boolean;
}

export interface CatalogoView {
  inicial: string;
  series: SerieCatalogoView[];
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
