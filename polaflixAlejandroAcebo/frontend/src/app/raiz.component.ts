import { Component } from '@angular/core';

import { UserSessionService } from './servicios/servicio-sesion-usuario';

@Component({
  selector: 'app-root',
  templateUrl: './raiz.component.html',
  styleUrls: ['./raiz.component.css']
})
export class AppComponent {
  readonly userName = this.userSession.usuarioNombre;

  constructor(private readonly userSession: UserSessionService) {}
}
