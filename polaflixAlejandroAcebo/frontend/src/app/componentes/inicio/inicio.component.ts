import { Component } from '@angular/core';
import { catchError, map, Observable, of, startWith } from 'rxjs';

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
export class InicioComponent {
  readonly state$: Observable<UserHomeState> = this.seriesApiService.getUsuarioHome(this.userSession.usuarioId).pipe(
    map((view) => ({ status: 'success', view } as UserHomeState)),
    startWith({ status: 'loading' } as UserHomeState),
    catchError((error: Error) => of({ status: 'error', message: error.message } as UserHomeState))
  );

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}
}
