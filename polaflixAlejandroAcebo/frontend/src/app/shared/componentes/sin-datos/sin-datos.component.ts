import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  templateUrl: './sin-datos.component.html',
  styleUrls: ['./sin-datos.component.css']
})
export class EmptyStateComponent {
  @Input({ required: true }) title = '';
  @Input() text = '';
}
