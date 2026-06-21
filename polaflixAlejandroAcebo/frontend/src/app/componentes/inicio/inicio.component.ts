import { Component, OnInit } from '@angular/core';

import { UsuarioHomeView } from '../../modelos/modelo-pantalla-usuario';
import { SeriesApiService } from '../../servicios/servicio-series-api';
import { UserSessionService } from '../../servicios/servicio-sesion-usuario';

type UserHomeState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; view: UsuarioHomeView };

@Component({
  selector: 'app-user-home-page',
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent implements OnInit {
  state: UserHomeState = { status: 'loading' };

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}

  ngOnInit(): void {
    this.cargarInicio();
  }

  private cargarInicio(): void {
    this.state = { status: 'loading' };

    this.seriesApiService.getUsuarioHome(this.userSession.usuarioId).subscribe({
      next: (view) => this.state = { status: 'success', view },
      error: (error: Error) => this.state = { status: 'error', message: error.message }
    });
  }
}
