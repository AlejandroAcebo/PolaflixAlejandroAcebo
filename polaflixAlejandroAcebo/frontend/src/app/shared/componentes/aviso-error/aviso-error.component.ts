import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-error-alert',
  templateUrl: './aviso-error.component.html',
  styleUrls: ['./aviso-error.component.css']
})
export class ErrorAlertComponent {
  @Input({ required: true }) message = '';
}
