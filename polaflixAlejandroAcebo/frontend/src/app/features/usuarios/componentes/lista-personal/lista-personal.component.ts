import { Component, Input } from '@angular/core';

import { SeriePersonalView } from '../../../../core/modelos/modelo-pantalla-usuario';

@Component({
  selector: 'app-personal-list',
  templateUrl: './lista-personal.component.html',
  styleUrls: ['./lista-personal.component.css']
})
export class PersonalListComponent {
  @Input({ required: true }) title = '';
  @Input({ required: true }) items: SeriePersonalView[] = [];
  @Input() showProgress = true;

  progress(item: SeriePersonalView): number {
    if (item.totalCapitulos <= 0) {
      return 0;
    }

    return Math.min(100, Math.round((item.capitulosVistos / item.totalCapitulos) * 100));
  }

  stateLabel(item: SeriePersonalView): string {
    return item.seguimiento.estadoSerie.replace('_', ' ').toLowerCase();
  }

  metaText(item: SeriePersonalView): string {
    if (!this.showProgress) {
      return `${item.tipoSerie} - serie terminada`;
    }

    return `${item.tipoSerie} - ${item.capitulosVistos}/${item.totalCapitulos} capitulos`;
  }
}
