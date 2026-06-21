import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './rutas-app.module';
import { AppComponent } from './raiz.component';
import { apiHeadersInterceptor } from './interceptores/interceptor-cabeceras-api';
import { AvisoErrorComponent } from './compartido/aviso-error/aviso-error.component';
import { CargandoComponent } from './compartido/cargando/cargando.component';
import { CatalogoComponent } from './componentes/catalogo/catalogo.component';
import { DetalleSerieComponent } from './componentes/detalle-serie/detalle-serie.component';
import { FacturasComponent } from './componentes/facturas/facturas.component';
import { InicioComponent } from './componentes/inicio/inicio.component';
import { ListaPersonalComponent } from './componentes/lista-personal/lista-personal.component';
import { SinDatosComponent } from './compartido/sin-datos/sin-datos.component';

@NgModule({
  declarations: [
    AppComponent,
    AvisoErrorComponent,
    CargandoComponent,
    CatalogoComponent,
    DetalleSerieComponent,
    FacturasComponent,
    InicioComponent,
    ListaPersonalComponent,
    SinDatosComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [provideHttpClient(withInterceptors([apiHeadersInterceptor]))],
  bootstrap: [AppComponent]
})
export class AppModule {}
