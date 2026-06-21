import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CatalogoComponent } from './componentes/catalogo/catalogo.component';
import { DetalleSerieComponent } from './componentes/detalle-serie/detalle-serie.component';
import { FacturasComponent } from './componentes/facturas/facturas.component';
import { InicioComponent } from './componentes/inicio/inicio.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'usuario' },
  { path: 'usuario', component: InicioComponent },
  { path: 'catalogo', component: CatalogoComponent },
  { path: 'series/:serieId', component: DetalleSerieComponent },
  { path: 'facturas', component: FacturasComponent },
  { path: '**', redirectTo: 'usuario' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {}
