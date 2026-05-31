import { Component } from '@angular/core';

import { UserSessionService } from '../../core/servicios/servicio-sesion-usuario';

@Component({
  selector: 'app-layout',
  templateUrl: './plantilla-app.component.html',
  styleUrls: ['./plantilla-app.component.css']
})
export class AppLayoutComponent {
  readonly userName = this.userSession.usuarioNombre;
  readonly userInitial = this.userName.trim().charAt(0).toUpperCase() || 'P';

  constructor(private readonly userSession: UserSessionService) {}
}
