import { Component, Input } from '@angular/core';

import { SeriePersonalView } from '../../modelos/modelo-pantalla-usuario';

@Component({
  selector: 'app-personal-list',
  templateUrl: './lista-personal.component.html',
  styleUrls: ['./lista-personal.component.css']
})
export class ListaPersonalComponent {
  @Input({ required: true }) title = '';
  @Input({ required: true }) items: SeriePersonalView[] = [];
}
