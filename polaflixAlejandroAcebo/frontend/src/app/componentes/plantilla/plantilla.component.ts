import { Component } from '@angular/core';

import { UserSessionService } from '../../servicios/servicio-sesion-usuario';

@Component({
  selector: 'app-layout',
  templateUrl: './plantilla.component.html',
  styleUrls: ['./plantilla.component.css']
})
export class PlantillaComponent {
  readonly userName = this.userSession.usuarioNombre;

  constructor(private readonly userSession: UserSessionService) {}
}
