import { Component, EventEmitter, Input, Output } from '@angular/core';

import { CapituloConEstado } from '../../../../core/modelos/modelo-capitulo';

@Component({
  selector: 'app-episode-item',
  templateUrl: './episodio.component.html',
  styleUrls: ['./episodio.component.css']
})
export class EpisodeItemComponent {
  @Input({ required: true }) capitulo!: CapituloConEstado;
  @Input() saving = false;
  @Output() markAsViewed = new EventEmitter<number>();

  descriptionVisible = false;

  toggleDescription(): void {
    this.descriptionVisible = !this.descriptionVisible;
  }

  viewEpisode(): void {
    if (!this.capitulo.visto && !this.saving) {
      this.markAsViewed.emit(this.capitulo.idCapitulo);
    }
  }
}
