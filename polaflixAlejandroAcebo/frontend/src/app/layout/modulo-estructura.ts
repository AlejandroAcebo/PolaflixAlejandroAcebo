import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../shared/modulo-compartido';
import { AppLayoutComponent } from './plantilla-app/plantilla-app.component';

@NgModule({
  declarations: [AppLayoutComponent],
  imports: [RouterModule, SharedModule],
  exports: [AppLayoutComponent]
})
export class LayoutModule {}
