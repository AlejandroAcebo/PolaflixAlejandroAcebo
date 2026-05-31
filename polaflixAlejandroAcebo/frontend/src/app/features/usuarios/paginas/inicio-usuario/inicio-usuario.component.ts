import { Component } from '@angular/core';
import { catchError, map, Observable, of, startWith } from 'rxjs';

import { UsuarioHomeView } from '../../../../core/modelos/modelo-pantalla-usuario';
import { SeriesApiService } from '../../../../core/servicios/servicio-series-api';
import { UserSessionService } from '../../../../core/servicios/servicio-sesion-usuario';

type UserHomeState =
  | { status: 'loading' }
  | { status: 'error'; message: string }
  | { status: 'success'; view: UsuarioHomeView };

@Component({
  selector: 'app-user-home-page',
  templateUrl: './inicio-usuario.component.html',
  styleUrls: ['./inicio-usuario.component.css']
})
export class UserHomePageComponent {
  readonly state$: Observable<UserHomeState> = this.seriesApiService.getUsuarioHome(this.userSession.usuarioId).pipe(
    map((view) => ({ status: 'success', view } as UserHomeState)),
    startWith({ status: 'loading' } as UserHomeState),
    catchError((error: Error) => of({ status: 'error', message: error.message } as UserHomeState))
  );

  constructor(
    private readonly seriesApiService: SeriesApiService,
    private readonly userSession: UserSessionService
  ) {}

  totalSeries(view: UsuarioHomeView): number {
    return view.empezadas.length + view.pendientes.length + view.terminadas.length;
  }

  totalCapitulosVistos(view: UsuarioHomeView): number {
    return [...view.empezadas, ...view.pendientes, ...view.terminadas]
      .reduce((total, serie) => total + serie.capitulosVistos, 0);
  }
}
