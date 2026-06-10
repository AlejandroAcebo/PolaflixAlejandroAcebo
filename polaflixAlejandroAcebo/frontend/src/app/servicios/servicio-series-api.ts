import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';

import { CatalogoView, FacturaMensualView, SerieCatalogoView, SerieDetalleView, UsuarioHomeView } from '../modelos/modelo-pantalla-usuario';
import { ErrorMessageService } from './servicio-mensaje-error';

@Injectable({ providedIn: 'root' })
export class SeriesApiService {
  private readonly apiUrl = '';

  constructor(
    private readonly http: HttpClient,
    private readonly errorMessageService: ErrorMessageService
  ) {}

  getUsuarioHome(usuarioId: number): Observable<UsuarioHomeView> {
    return this.http
      .get<UsuarioHomeView>(`${this.apiUrl}/usuarios/${usuarioId}/home`)
      .pipe(catchError((error) => this.errorMessageService.toObservableError(error)));
  }

  getCatalogo(usuarioId: number, inicial: string): Observable<CatalogoView> {
    return this.http
      .get<CatalogoView>(`${this.apiUrl}/usuarios/${usuarioId}/catalogo/${inicial}`)
      .pipe(catchError((error) => this.errorMessageService.toObservableError(error)));
  }

  buscarSerieCatalogo(usuarioId: number, nombre: string): Observable<SerieCatalogoView> {
    return this.http
      .get<SerieCatalogoView>(`${this.apiUrl}/usuarios/${usuarioId}/catalogo-buscar?nombre=${encodeURIComponent(nombre)}`)
      .pipe(catchError((error) => this.errorMessageService.toObservableError(error)));
  }

  getSerieDetalle(usuarioId: number, serieId: number): Observable<SerieDetalleView> {
    return this.http
      .get<SerieDetalleView>(`${this.apiUrl}/usuarios/${usuarioId}/series/${serieId}/detalle`)
      .pipe(catchError((error) => this.errorMessageService.toObservableError(error)));
  }

  marcarCapituloComoVisto(usuarioId: number, idCapitulo: number): Observable<void> {
    return this.http
      .post<void>(`${this.apiUrl}/usuarios/${usuarioId}/visualizaciones`, { idCapitulo })
      .pipe(catchError((error) => this.errorMessageService.toObservableError(error)));
  }

  getFacturaMensual(usuarioId: number, anio: number, mes: number): Observable<FacturaMensualView> {
    return this.http
      .get<FacturaMensualView>(`${this.apiUrl}/usuarios/${usuarioId}/facturas/${anio}/${mes}`)
      .pipe(catchError((error) => this.errorMessageService.toObservableError(error)));
  }
}
