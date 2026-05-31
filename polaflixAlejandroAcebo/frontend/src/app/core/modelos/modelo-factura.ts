export interface CargoFactura {
  fecha: string;
  nombreSerie: string;
  episodio: string;
  precio: number;
}

export interface FacturaMensual {
  monthDate: Date;
  cargos: CargoFactura[];
  total: number;
  cuotaFija: boolean;
}
