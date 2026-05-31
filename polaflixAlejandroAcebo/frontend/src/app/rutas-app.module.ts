import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { IntroPageComponent } from './features/inicio-app/paginas/inicio-app/inicio-app.component';
import { SeriesDetailPageComponent } from './features/series/paginas/detalle-serie/detalle-serie.component';
import { SeriesListPageComponent } from './features/series/paginas/lista-series/lista-series.component';
import { BillingPageComponent } from './features/usuarios/paginas/facturas/facturas.component';
import { UserHomePageComponent } from './features/usuarios/paginas/inicio-usuario/inicio-usuario.component';
import { AppLayoutComponent } from './layout/plantilla-app/plantilla-app.component';

const routes: Routes = [
  { path: 'intro', component: IntroPageComponent },
  { path: '', pathMatch: 'full', redirectTo: 'intro' },
  {
    path: '',
    component: AppLayoutComponent,
    children: [
      { path: 'usuario', component: UserHomePageComponent },
      { path: 'catalogo', component: SeriesListPageComponent },
      { path: 'series/:serieId', component: SeriesDetailPageComponent },
      { path: 'facturas', component: BillingPageComponent }
    ]
  },
  { path: '**', redirectTo: 'intro' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { bindToComponentInputs: true, useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
