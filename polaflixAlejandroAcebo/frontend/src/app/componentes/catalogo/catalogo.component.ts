import { Component, OnInit } from '@angular/core';

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
export class CatalogoComponent implements OnInit {
  readonly letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0-9'.split('');

  activeLetter = 'A';
  actionError: string | null = null;
  selectedSerieId: number | null = null;
  state: SeriesListState = { status: 'loading' };

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly seguimientosApiService: SeguimientosApiService,
    private readonly userSession: UserSessionService
  ) {}

  ngOnInit(): void {
    this.cargarCatalogo();
  }

  setLetter(letter: string): void {
    this.activeLetter = letter;
    this.actionError = null;
    this.selectedSerieId = null;
    this.cargarCatalogo();
  }

  search(term: string, event?: Event): void {
    event?.preventDefault();

    const normalizedTerm = term.trim().toLowerCase();
    if (!normalizedTerm) {
      return;
    }

    this.seriesApiService.buscarSerieCatalogo(this.userSession.usuarioId, normalizedTerm).subscribe({
      next: (found) => {
        this.activeLetter = this.getInitial(found.nombreSerie);
        this.actionError = null;
        this.selectedSerieId = found.idSerie;
        this.cargarCatalogo();
      },
      error: (error: Error) => this.actionError = error.message
    });
  }

  addSerie(serie: SerieCatalogoView): void {
    if (serie.agregada) {
      return;
    }

    this.seguimientosApiService.addSeriePendiente(this.userSession.usuarioId, serie.idSerie).subscribe({
      next: () => {
        this.actionError = null;
        this.cargarCatalogo();
      },
      error: (error: Error) => this.actionError = error.message
    });
  }

  toggleDetalle(serieId: number): void {
    this.selectedSerieId = this.selectedSerieId === serieId ? null : serieId;
  }

  private cargarCatalogo(): void {
    this.state = { status: 'loading' };

    this.seriesApiService.getCatalogo(this.userSession.usuarioId, this.activeLetter).subscribe({
      next: (view) => this.state = { status: 'success', view },
      error: (error: Error) => this.state = { status: 'error', message: error.message }
    });
  }

  private getInitial(name: string): string {
    const firstChar = name.trim().charAt(0).toUpperCase();
    return /^[A-Z]$/.test(firstChar) ? firstChar : '0-9';
  }
}
