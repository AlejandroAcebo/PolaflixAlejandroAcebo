import { Component, EventEmitter, Input, Output } from '@angular/core';

import { TemporadaConCapitulos } from '../../../../core/modelos/modelo-temporada';

@Component({
  selector: 'app-season-section',
  templateUrl: './temporada.component.html',
  styleUrls: ['./temporada.component.css']
})
export class SeasonSectionComponent {
  @Input({ required: true }) temporada!: TemporadaConCapitulos;
  @Input() savingEpisodeId: number | null = null;
  @Output() markEpisodeAsViewed = new EventEmitter<number>();
}
