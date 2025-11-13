import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BeneficiarioService } from '../../services/beneficiario.service';
import { TipoDonacion, BeneficiarioRegistroRequest } from '../../models/models';

@Component({
  selector: 'app-registro-beneficiario',
  templateUrl: './registro-beneficiario.component.html',
  styleUrls: ['./registro-beneficiario.component.css']
})
export class RegistroBeneficiarioComponent implements OnInit {
  registroForm: FormGroup;
  tiposDonacion = Object.values(TipoDonacion);
  loading = false;
  ayudasSeleccionadas: Set<TipoDonacion> = new Set();

  constructor(
    private fb: FormBuilder,
    private beneficiarioService: BeneficiarioService,
    private router: Router
  ) {
    this.registroForm = this.fb.group({
      nombre: ['', Validators.required],
      cedula: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
      telefono: ['', Validators.required],
      email: ['', [Validators.email]],
      direccion: ['', Validators.required],
      ciudad: ['', Validators.required],
      descripcionSituacion: ['', [Validators.maxLength(500)]],
      personasEnHogar: [1, [Validators.required, Validators.min(1)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmarPassword: ['', Validators.required],
      aceptaTerminos: [false, Validators.requiredTrue]
    });
  }

  ngOnInit(): void {}

  toggleAyuda(tipo: TipoDonacion): void {
    if (this.ayudasSeleccionadas.has(tipo)) {
      this.ayudasSeleccionadas.delete(tipo);
    } else {
      this.ayudasSeleccionadas.add(tipo);
    }
  }

  isAyudaSeleccionada(tipo: TipoDonacion): boolean {
    return this.ayudasSeleccionadas.has(tipo);
  }

  onSubmit(): void {
    if (this.registroForm.valid && this.ayudasSeleccionadas.size > 0) {
      if (this.registroForm.value.password !== this.registroForm.value.confirmarPassword) {
        alert('Las contraseñas no coinciden');
        return;
      }

      this.loading = true;
      const request: BeneficiarioRegistroRequest = {
        ...this.registroForm.value,
        ayudasNecesarias: Array.from(this.ayudasSeleccionadas)
      };

      this.beneficiarioService.registrar(request).subscribe({
        next: (response) => {
          this.loading = false;
          alert('Registro exitoso. Ahora puedes iniciar sesión.');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.loading = false;
          console.error('Error al registrar:', error);
          alert('Error al registrar. Por favor intente nuevamente.');
        }
      });
    } else if (this.ayudasSeleccionadas.size === 0) {
      alert('Debe seleccionar al menos un tipo de ayuda necesaria');
    }
  }

  irALogin(): void {
    this.router.navigate(['/login']);
  }
}
