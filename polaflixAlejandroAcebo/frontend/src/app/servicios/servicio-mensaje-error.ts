import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ErrorMessageService {
  toObservableError(error: unknown, accion = 'completar la operacion'): Observable<never> {
    return throwError(() => new Error(this.fromHttpError(error, accion)));
  }

  fromHttpError(error: unknown, accion = 'completar la operacion'): string {
    if (!(error instanceof HttpErrorResponse)) {
      return `No se ha podido ${accion}: ha ocurrido un error inesperado.`;
    }

    if (error.status === 0) {
      return 'No se ha podido conectar con la API. Comprueba que Spring Boot esta arrancado en el puerto 8080.';
    }

    if (typeof error.error?.message === 'string') {
      return error.error.message;
    }

    if (error.status === 400) {
      return `No se ha podido ${accion}: la peticion enviada no es valida.`;
    }

    if (error.status === 404) {
      return `No se ha podido ${accion}: no se ha encontrado el recurso solicitado.`;
    }

    if (error.status === 409) {
      return `No se ha podido ${accion}: hay un conflicto con los datos actuales.`;
    }

    if (error.status >= 500) {
      return `No se ha podido ${accion}: la API ha devuelto un error interno.`;
    }

    return `No se ha podido ${accion}. Error ${error.status}.`;
  }
}
