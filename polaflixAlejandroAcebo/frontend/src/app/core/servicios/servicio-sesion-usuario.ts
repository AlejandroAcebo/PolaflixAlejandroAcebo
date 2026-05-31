import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class UserSessionService {
  private readonly defaultUsuarioId = 1;
  private readonly defaultUsuarioNombre = 'Alejandro Acebo';

  get usuarioId(): number {
    return this.defaultUsuarioId;
  }

  get usuarioNombre(): string {
    return this.defaultUsuarioNombre;
  }
}
