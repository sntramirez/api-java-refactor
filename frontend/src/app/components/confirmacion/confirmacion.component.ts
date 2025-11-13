import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DonacionResponse } from '../../models/models';

@Component({
  selector: 'app-confirmacion',
  templateUrl: './confirmacion.component.html',
  styleUrls: ['./confirmacion.component.css']
})
export class ConfirmacionComponent implements OnInit {
  donacion: DonacionResponse | null = null;

  constructor(private router: Router) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras?.state) {
      this.donacion = navigation.extras.state['donacion'];
    }
  }

  ngOnInit(): void {
    if (!this.donacion) {
      this.router.navigate(['/home']);
    }
  }

  volverAlInicio(): void {
    this.router.navigate(['/home']);
  }

  hacerOtraDonacion(): void {
    this.router.navigate(['/home']);
    setTimeout(() => {
      document.getElementById('formulario-donacion')?.scrollIntoView({ behavior: 'smooth' });
    }, 100);
  }
}
