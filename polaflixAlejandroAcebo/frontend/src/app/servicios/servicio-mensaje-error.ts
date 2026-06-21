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
      return 'No se ha podido conectar con la API. Comprueba que Spring Boot esta arrancado en el puerto 8080.';
    }

    if (typeof error.error?.message === 'string') {
      return error.error.message;
    }

    if (error.status === 400) {
      return 'La peticion enviada no es valida. Revisa los datos introducidos.';
    }

    if (error.status === 404) {
      return 'No se ha encontrado el recurso solicitado.';
    }

    if (error.status === 409) {
      return 'La operacion no se puede completar porque entra en conflicto con los datos actuales.';
    }

    if (error.status >= 500) {
      return 'La API ha devuelto un error interno. Revisa el backend.';
    }

    return `Error ${error.status}: no se ha podido completar la operacion.`;
  }
}
