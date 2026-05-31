import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/modulo-compartido';
import { EpisodeItemComponent } from './componentes/episodio/episodio.component';
import { SeasonSectionComponent } from './componentes/temporada/temporada.component';
import { SeriesDetailPageComponent } from './paginas/detalle-serie/detalle-serie.component';
import { SeriesListPageComponent } from './paginas/lista-series/lista-series.component';

@NgModule({
  declarations: [
    EpisodeItemComponent,
    SeasonSectionComponent,
    SeriesDetailPageComponent,
    SeriesListPageComponent
  ],
  imports: [RouterModule, SharedModule],
  exports: [SeriesDetailPageComponent, SeriesListPageComponent]
})
export class SeriesModule {}
