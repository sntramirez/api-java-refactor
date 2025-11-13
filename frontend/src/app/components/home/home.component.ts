import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DonacionService } from '../../services/donacion.service';
import { TipoDonacion, DonacionRequest } from '../../models/models';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  donacionForm: FormGroup;
  tiposDonacion = Object.values(TipoDonacion);
  loading = false;

  estadisticas = {
    totalDonaciones: 0,
    beneficiariosAyudados: 0
  };

  campanasActivas = [
    {
      nombre: 'Ayuda Alimentaria Diciembre',
      descripcion: 'Recolección de alimentos no perecibles',
      tipoAyuda: 'Comida',
      progreso: 65
    },
    {
      nombre: 'Medicamentos Esenciales',
      descripcion: 'Necesitamos medicamentos básicos',
      tipoAyuda: 'Medicamentos',
      progreso: 40
    },
    {
      nombre: 'Ropa de Invierno',
      descripcion: 'Ropa abrigada para familias',
      tipoAyuda: 'Ropa',
      progreso: 80
    }
  ];

  constructor(
    private fb: FormBuilder,
    private donacionService: DonacionService,
    private router: Router
  ) {
    this.donacionForm = this.fb.group({
      tipo: ['', Validators.required],
      cantidad: ['', Validators.required],
      descripcion: [''],
      nombreDonante: ['', Validators.required],
      telefonoDonante: ['', Validators.required],
      emailDonante: ['', [Validators.required, Validators.email]],
      comentarios: ['']
    });
  }

  ngOnInit(): void {
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    // Aquí puedes llamar a un endpoint para obtener estadísticas reales
    this.estadisticas = {
      totalDonaciones: 1250,
      beneficiariosAyudados: 450
    };
  }

  scrollToDonationForm(): void {
    document.getElementById('formulario-donacion')?.scrollIntoView({ behavior: 'smooth' });
  }

  onSubmit(): void {
    if (this.donacionForm.valid) {
      this.loading = true;
      const request: DonacionRequest = this.donacionForm.value;

      this.donacionService.crearDonacion(request).subscribe({
        next: (response) => {
          this.loading = false;
          this.router.navigate(['/confirmacion'], {
            state: { donacion: response }
          });
        },
        error: (error) => {
          this.loading = false;
          console.error('Error al crear donación:', error);
          alert('Error al procesar la donación. Por favor intente nuevamente.');
        }
      });
    }
  }
}
