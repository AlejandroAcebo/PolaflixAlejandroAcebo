import { Component } from '@angular/core';
import { BehaviorSubject, catchError, combineLatest, map, Observable, of, startWith, switchMap, tap } from 'rxjs';

import { SerieCatalogoView } from '../../modelos/modelo-pantalla-usuario';
import { SeguimientosApiService } from '../../servicios/servicio-seguimientos-api';
import { SeriesApiService } from '../../servicios/servicio-series-api';
import { UserSessionService } from '../../servicios/servicio-sesion-usuario';

type SeriesListState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; view: CatalogView };

interface CatalogView {
  activeLetter: string;
  highlightedSerieId: number | null;
  series: SerieCatalogoView[];
}

@Component({
  selector: 'app-series-list-page',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent {
  readonly letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0-9'.split('');
  readonly expandedSerieId$ = new BehaviorSubject<number | null>(null);

  private readonly activeLetterSubject = new BehaviorSubject<string>('A');
  private readonly highlightedSerieIdSubject = new BehaviorSubject<number | null>(null);
  private readonly refreshSubject = new BehaviorSubject<void>(undefined);

  readonly activeLetter$ = this.activeLetterSubject.asObservable();

  readonly state$: Observable<SeriesListState> = combineLatest([
    this.activeLetterSubject,
    this.highlightedSerieIdSubject,
    this.refreshSubject
  ]).pipe(
    switchMap(([activeLetter, highlightedSerieId]) =>
      this.seriesApiService.getCatalogo(this.userSession.usuarioId, activeLetter).pipe(
        map((catalogo) => ({
          status: 'success',
          view: {
            activeLetter: catalogo.inicial,
            highlightedSerieId,
            series: catalogo.series
          }
        }) as SeriesListState)
      )
    ),
    startWith({ status: 'loading' } as SeriesListState),
    catchError((error: Error) => of({ status: 'error', message: error.message } as SeriesListState))
  );

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly seguimientosApiService: SeguimientosApiService,
    private readonly userSession: UserSessionService
  ) {}

  setLetter(letter: string): void {
    this.highlightedSerieIdSubject.next(null);
    this.activeLetterSubject.next(letter);
  }

  search(term: string, event?: Event): void {
    event?.preventDefault();

    const normalizedTerm = term.trim().toLowerCase();

    if (!normalizedTerm) {
      return;
    }

    this.seriesApiService.buscarSerieCatalogo(this.userSession.usuarioId, normalizedTerm).subscribe({
      next: (found) => {
        this.expandedSerieId$.next(found.idSerie);
        this.highlightedSerieIdSubject.next(found.idSerie);
        this.activeLetterSubject.next(this.getInitial(found.nombreSerie));
      },
      error: (error: Error) => {
        this.expandedSerieId$.next(null);
        this.highlightedSerieIdSubject.next(null);
        window.alert(error.message);
      }
    });
  }

  addSerie(serie: SerieCatalogoView): void {
    if (serie.agregada) {
      return;
    }

    this.seguimientosApiService
      .addSeriePendiente(this.userSession.usuarioId, serie.idSerie)
      .pipe(tap(() => this.refreshSubject.next()))
      .subscribe({
        error: (error: Error) => window.alert(error.message)
      });
  }

  toggleSynopsis(serieId: number): void {
    const nextId = this.expandedSerieId$.value === serieId ? null : serieId;
    this.expandedSerieId$.next(nextId);
  }

  private getInitial(name: string): string {
    const firstChar = name.trim().charAt(0).toUpperCase();
    return /^[A-Z]$/.test(firstChar) ? firstChar : '0-9';
  }
}
