import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, catchError, combineLatest, map, Observable, of, startWith, switchMap, tap } from 'rxjs';

import { SerieDetalleView, TemporadaDetalleView } from '../../modelos/modelo-pantalla-usuario';
import { SeriesApiService } from '../../servicios/servicio-series-api';
import { UserSessionService } from '../../servicios/servicio-sesion-usuario';

type DetailState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; view: SerieDetalleView };

@Component({
  selector: 'app-series-detail-page',
  templateUrl: './detalle-serie.component.html',
  styleUrls: ['./detalle-serie.component.css']
})
export class DetalleSerieComponent {
  readonly temporadaIndex$ = new BehaviorSubject<number>(0);
  readonly descripcionCapituloId$ = new BehaviorSubject<number | null>(null);

  private readonly refresh$ = new BehaviorSubject<void>(undefined);
  private readonly serieId$ = this.route.paramMap.pipe(map((params) => Number(params.get('serieId'))));

  readonly state$: Observable<DetailState> = combineLatest([this.serieId$, this.refresh$]).pipe(
    switchMap(([serieId]) =>
      this.seriesApiService.getSerieDetalle(this.userSession.usuarioId, serieId).pipe(
        tap((view) => this.temporadaIndex$.next(view.temporadaInicial)),
        map((view) => ({ status: 'success', view } as DetailState))
      )
    ),
    startWith({ status: 'loading' } as DetailState),
    catchError((error: Error) => of({ status: 'error', message: error.message } as DetailState))
  );

  constructor(
    private readonly route: ActivatedRoute,
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}

  temporadaSeleccionada(temporadas: TemporadaDetalleView[], index: number): TemporadaDetalleView | null {
    return temporadas[index] ?? null;
  }

  cambiarTemporada(cantidad: number, total: number): void {
    const siguiente = this.temporadaIndex$.value + cantidad;
    if (siguiente >= 0 && siguiente < total) {
      this.descripcionCapituloId$.next(null);
      this.temporadaIndex$.next(siguiente);
    }
  }

  mostrarDescripcion(idCapitulo: number): void {
    this.descripcionCapituloId$.next(
      this.descripcionCapituloId$.value === idCapitulo ? null : idCapitulo
    );
  }

  marcarVisto(idCapitulo: number): void {
    this.seriesApiService
      .marcarCapituloComoVisto(this.userSession.usuarioId, idCapitulo)
      .subscribe({
        next: () => this.refresh$.next(),
        error: (error: Error) => window.alert(error.message)
      });
  }
}
