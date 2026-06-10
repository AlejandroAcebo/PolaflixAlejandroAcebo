import { Component } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, startWith, switchMap } from 'rxjs';

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
export class FacturasComponent {
  private readonly fechaSubject = new BehaviorSubject<Date>(new Date());

  readonly state$: Observable<BillingState> = this.fechaSubject.pipe(
    switchMap((fecha) =>
      this.seriesApiService.getFacturaMensual(
        this.userSession.usuarioId,
        fecha.getFullYear(),
        fecha.getMonth() + 1
      ).pipe(map((factura) => ({ status: 'success', factura, fecha } as BillingState)))
    ),
    startWith({ status: 'loading' } as BillingState),
    catchError((error: Error) => of({ status: 'error', message: error.message } as BillingState))
  );

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}

  cambiarMes(cantidad: number): void {
    const fecha = this.fechaSubject.value;
    this.fechaSubject.next(new Date(fecha.getFullYear(), fecha.getMonth() + cantidad, 1));
  }
}
