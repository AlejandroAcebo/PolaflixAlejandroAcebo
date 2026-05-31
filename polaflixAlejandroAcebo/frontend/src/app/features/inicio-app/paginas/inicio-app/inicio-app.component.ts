import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-intro-page',
  templateUrl: './inicio-app.component.html',
  styleUrls: ['./inicio-app.component.css']
})
export class IntroPageComponent implements OnInit {
  constructor(private readonly router: Router) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.goHome();
    }, 3200);
  }

  goHome(): void {
    this.router.navigateByUrl('/usuario');
  }
}
