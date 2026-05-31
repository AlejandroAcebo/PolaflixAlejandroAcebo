import { Component } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, of, startWith, switchMap } from 'rxjs';

import { FacturaMensual } from '../../../../core/modelos/modelo-factura';
import { SeriesApiService } from '../../../../core/servicios/servicio-series-api';
import { UserSessionService } from '../../../../core/servicios/servicio-sesion-usuario';

type BillingState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; factura: FacturaMensual };

@Component({
  selector: 'app-billing-page',
  templateUrl: './facturas.component.html',
  styleUrls: ['./facturas.component.css']
})
export class BillingPageComponent {
  private readonly displayedMonthSubject = new BehaviorSubject<Date>(new Date());

  readonly state$: Observable<BillingState> = this.displayedMonthSubject.pipe(
    switchMap((monthDate) =>
      this.seriesApiService
        .getFacturaMensual(this.userSession.usuarioId, monthDate.getFullYear(), monthDate.getMonth() + 1)
        .pipe(
          map((factura) => ({
            status: 'success',
            factura: {
              monthDate,
              cargos: factura.cargos,
              cuotaFija: factura.cuotaFija,
              total: factura.total
            }
          }) as BillingState)
        )
    ),
    startWith({ status: 'loading' } as BillingState),
    catchError((error: Error) => of({ status: 'error', message: error.message } as BillingState))
  );

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}

  previousMonth(): void {
    this.moveMonth(-1);
  }

  nextMonth(): void {
    this.moveMonth(1);
  }

  private moveMonth(offset: number): void {
    const current = this.displayedMonthSubject.value;
    this.displayedMonthSubject.next(new Date(current.getFullYear(), current.getMonth() + offset, 1));
  }
}
