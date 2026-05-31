import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ErrorMessageService {
  toObservableError(error: unknown): Observable<never> {
    return throwError(() => new Error(this.fromHttpError(error)));
  }

  fromHttpError(error: unknown): string {
    if (!(error instanceof HttpErrorResponse)) {
      return 'Ha ocurrido un error inesperado.';
    }

    if (error.status === 0) {
      return 'No se ha podido conectar con la API. Comprueba que Spring Boot esta arrancado en el puerto 8081.';
    }

    if (typeof error.error?.message === 'string') {
      return error.error.message;
    }

    return `Error ${error.status}: no se ha podido completar la operacion.`;
  }
}
