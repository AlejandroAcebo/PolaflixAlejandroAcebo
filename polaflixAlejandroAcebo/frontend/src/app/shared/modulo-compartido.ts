import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { EmptyStateComponent } from './componentes/sin-datos/sin-datos.component';
import { ErrorAlertComponent } from './componentes/aviso-error/aviso-error.component';
import { LoadingStateComponent } from './componentes/cargando/cargando.component';

@NgModule({
  declarations: [EmptyStateComponent, ErrorAlertComponent, LoadingStateComponent],
  imports: [CommonModule],
  exports: [CommonModule, EmptyStateComponent, ErrorAlertComponent, LoadingStateComponent]
})
export class SharedModule {}
