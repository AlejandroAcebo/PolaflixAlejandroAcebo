import { Component } from '@angular/core';
import { BehaviorSubject, catchError, combineLatest, map, Observable, of, startWith, switchMap, tap } from 'rxjs';

import { CatalogoView, SerieCatalogoView } from '../../modelos/modelo-pantalla-usuario';
import { SeguimientosApiService } from '../../servicios/servicio-seguimientos-api';
import { SeriesApiService } from '../../servicios/servicio-series-api';
import { UserSessionService } from '../../servicios/servicio-sesion-usuario';

type SeriesListState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; view: CatalogoView };

@Component({
  selector: 'app-series-list-page',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent {
  readonly letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0-9'.split('');
  actionError: string | null = null;
  selectedSerieId: number | null = null;

  private readonly activeLetterSubject = new BehaviorSubject<string>('A');
  private readonly refreshSubject = new BehaviorSubject<void>(undefined);

  readonly activeLetter$ = this.activeLetterSubject.asObservable();

  readonly state$: Observable<SeriesListState> = combineLatest([
    this.activeLetterSubject,
    this.refreshSubject
  ]).pipe(
    switchMap(([activeLetter]) =>
      this.seriesApiService.getCatalogo(this.userSession.usuarioId, activeLetter).pipe(
        map((catalogo) => ({
          status: 'success',
          view: catalogo
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
    this.actionError = null;
    this.selectedSerieId = null;
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
        this.actionError = null;
        this.selectedSerieId = found.idSerie;
        this.activeLetterSubject.next(this.getInitial(found.nombreSerie));
      },
      error: (error: Error) => {
        this.actionError = error.message;
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
        next: () => this.actionError = null,
        error: (error: Error) => this.actionError = error.message
      });
  }

  toggleDetalle(serieId: number): void {
    this.selectedSerieId = this.selectedSerieId === serieId ? null : serieId;
  }

  private getInitial(name: string): string {
    const firstChar = name.trim().charAt(0).toUpperCase();
    return /^[A-Z]$/.test(firstChar) ? firstChar : '0-9';
  }
}
