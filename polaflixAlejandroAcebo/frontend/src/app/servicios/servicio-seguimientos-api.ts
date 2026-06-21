import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';

import { SeguimientoSerieRequest } from '../modelos/modelo-seguimiento-serie';
import { ErrorMessageService } from './servicio-mensaje-error';

@Injectable({ providedIn: 'root' })
export class SeguimientosApiService {
  constructor(
    private readonly http: HttpClient,
    private readonly errorMessageService: ErrorMessageService
  ) {}

  addSeriePendiente(usuarioId: number, idSerie: number): Observable<void> {
    const body: SeguimientoSerieRequest = { idSerie };

    return this.http
      .post<void>(`/usuarios/${usuarioId}/seguimientos`, body)
      .pipe(catchError((error) => this.errorMessageService.toObservableError(error, 'agregar la serie a pendientes')));
  }
}
