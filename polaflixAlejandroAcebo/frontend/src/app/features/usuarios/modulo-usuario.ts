import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/modulo-compartido';
import { PersonalListComponent } from './componentes/lista-personal/lista-personal.component';
import { BillingPageComponent } from './paginas/facturas/facturas.component';
import { UserHomePageComponent } from './paginas/inicio-usuario/inicio-usuario.component';

@NgModule({
  declarations: [BillingPageComponent, PersonalListComponent, UserHomePageComponent],
  imports: [RouterModule, SharedModule],
  exports: [BillingPageComponent, UserHomePageComponent]
})
export class UserModule {}
