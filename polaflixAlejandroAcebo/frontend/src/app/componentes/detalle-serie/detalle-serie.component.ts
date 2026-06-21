import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

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
export class DetalleSerieComponent implements OnInit {
  temporadaIndex = 0;
  actionError: string | null = null;
  selectedCapituloId: number | null = null;
  state: DetailState = { status: 'loading' };

  private serieId = 0;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}

  ngOnInit(): void {
    this.serieId = Number(this.route.snapshot.paramMap.get('serieId'));
    this.cargarDetalle();
  }

  temporadaSeleccionada(temporadas: TemporadaDetalleView[], index: number): TemporadaDetalleView | null {
    return temporadas[index] ?? null;
  }

  cambiarTemporada(cantidad: number, total: number): void {
    const siguiente = this.temporadaIndex + cantidad;
    if (siguiente >= 0 && siguiente < total) {
      this.actionError = null;
      this.selectedCapituloId = null;
      this.temporadaIndex = siguiente;
    }
  }

  toggleDescripcion(idCapitulo: number): void {
    this.selectedCapituloId = this.selectedCapituloId === idCapitulo ? null : idCapitulo;
  }

  marcarVisto(idCapitulo: number): void {
    this.seriesApiService.marcarCapituloComoVisto(this.userSession.usuarioId, idCapitulo).subscribe({
      next: () => {
        this.actionError = null;
        this.cargarDetalle();
      },
      error: (error: Error) => this.actionError = error.message
    });
  }

  private cargarDetalle(): void {
    this.state = { status: 'loading' };

    this.seriesApiService.getSerieDetalle(this.userSession.usuarioId, this.serieId).subscribe({
      next: (view) => {
        this.temporadaIndex = view.temporadaInicial;
        this.state = { status: 'success', view };
      },
      error: (error: Error) => this.state = { status: 'error', message: error.message }
    });
  }
}
