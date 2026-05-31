import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BehaviorSubject, catchError, combineLatest, finalize, map, Observable, of, startWith, switchMap, tap } from 'rxjs';

import { SerieDetalleView } from '../../../../core/modelos/modelo-pantalla-usuario';
import { TemporadaConCapitulos } from '../../../../core/modelos/modelo-temporada';
import { SeriesApiService } from '../../../../core/servicios/servicio-series-api';
import { UserSessionService } from '../../../../core/servicios/servicio-sesion-usuario';

type SeriesDetailState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; view: SerieDetalleView };

@Component({
  selector: 'app-series-detail-page',
  templateUrl: './detalle-serie.component.html',
  styleUrls: ['./detalle-serie.component.css']
})
export class SeriesDetailPageComponent {
  private readonly refreshSubject = new BehaviorSubject<void>(undefined);
  readonly selectedSeasonIndex$ = new BehaviorSubject<number>(0);

  readonly savingEpisodeId$ = new BehaviorSubject<number | null>(null);
  readonly actionError$ = new BehaviorSubject<string | null>(null);

  readonly state$: Observable<SeriesDetailState> = combineLatest([
    this.route.paramMap.pipe(map((params) => Number(params.get('serieId')))),
    this.refreshSubject
  ]).pipe(
    switchMap(([serieId]) => this.loadDetail(serieId)),
    startWith({ status: 'loading' } as SeriesDetailState),
    catchError((error: Error) => of({ status: 'error', message: error.message } as SeriesDetailState))
  );

  constructor(
    private readonly route: ActivatedRoute,
    private readonly userSession: UserSessionService,
    private readonly seriesApiService: SeriesApiService
  ) {}

  markAsViewed(idCapitulo: number): void {
    this.actionError$.next(null);
    this.savingEpisodeId$.next(idCapitulo);

    this.seriesApiService
      .marcarCapituloComoVisto(this.userSession.usuarioId, idCapitulo)
      .pipe(
        tap(() => this.refreshSubject.next()),
        finalize(() => this.savingEpisodeId$.next(null))
      )
      .subscribe({
        error: (error: Error) => this.actionError$.next(error.message)
      });
  }

  selectSeason(index: string): void {
    this.selectedSeasonIndex$.next(Number(index));
  }

  selectedSeason(temporadas: TemporadaConCapitulos[], selectedIndex: number): TemporadaConCapitulos | null {
    if (temporadas.length === 0) {
      return null;
    }

    const safeIndex = Math.min(Math.max(selectedIndex, 0), temporadas.length - 1);
    return temporadas[safeIndex];
  }

  private loadDetail(serieId: number): Observable<SeriesDetailState> {
    return this.seriesApiService.getSerieDetalle(this.userSession.usuarioId, serieId).pipe(
      map((view) => {
        this.selectedSeasonIndex$.next(view.temporadaInicial);
        return { status: 'success', view } as SeriesDetailState;
      })
    );
  }
}
