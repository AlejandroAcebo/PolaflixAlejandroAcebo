import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './rutas-app.module';
import { AppComponent } from './raiz.component';
import { apiHeadersInterceptor } from './core/interceptores/interceptor-cabeceras-api';
import { IntroPageComponent } from './features/inicio-app/paginas/inicio-app/inicio-app.component';
import { SeriesModule } from './features/series/modulo-series';
import { UserModule } from './features/usuarios/modulo-usuario';
import { LayoutModule } from './layout/modulo-estructura';

@NgModule({
  declarations: [AppComponent, IntroPageComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    LayoutModule,
    SeriesModule,
    UserModule
  ],
  providers: [provideHttpClient(withInterceptors([apiHeadersInterceptor]))],
  bootstrap: [AppComponent]
})
export class AppModule {}
