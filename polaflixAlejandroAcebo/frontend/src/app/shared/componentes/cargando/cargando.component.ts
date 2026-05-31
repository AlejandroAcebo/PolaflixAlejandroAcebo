import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-loading-state',
  templateUrl: './cargando.component.html',
  styleUrls: ['./cargando.component.css']
})
export class LoadingStateComponent {
  @Input() message = 'Cargando...';
}
