import { Component, OnInit } from '@angular/core';

import { FacturaMensualView } from '../../modelos/modelo-pantalla-usuario';
import { SeriesApiService } from '../../servicios/servicio-series-api';
import { UserSessionService } from '../../servicios/servicio-sesion-usuario';

type BillingState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; factura: FacturaMensualView; fecha: Date };

@Component({
  selector: 'app-billing-page',
  templateUrl: './facturas.component.html',
  styleUrls: ['./facturas.component.css']
})
export class FacturasComponent implements OnInit {
  state: BillingState = { status: 'loading' };

  private fecha = new Date();

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}

  ngOnInit(): void {
    this.cargarFactura();
  }

  cambiarMes(cantidad: number): void {
    this.fecha = new Date(this.fecha.getFullYear(), this.fecha.getMonth() + cantidad, 1);
    this.cargarFactura();
  }

  private cargarFactura(): void {
    this.state = { status: 'loading' };

    this.seriesApiService
      .getFacturaMensual(this.userSession.usuarioId, this.fecha.getFullYear(), this.fecha.getMonth() + 1)
      .subscribe({
        next: (factura) => this.state = { status: 'success', factura, fecha: this.fecha },
        error: (error: Error) => this.state = { status: 'error', message: error.message }
      });
  }
}
