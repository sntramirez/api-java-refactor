import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  tipoUsuario: 'BENEFICIARIO' | 'ADMINISTRADOR' = 'BENEFICIARIO';
  loading = false;

  constructor(
    private fb: FormBuilder,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      recordarme: [false]
    });
  }

  seleccionarTipo(tipo: 'BENEFICIARIO' | 'ADMINISTRADOR'): void {
    this.tipoUsuario = tipo;
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;

      // Simulación de login (aquí debes integrar con tu servicio de autenticación)
      setTimeout(() => {
        this.loading = false;

        if (this.tipoUsuario === 'BENEFICIARIO') {
          // Guardar datos de sesión
          localStorage.setItem('userType', 'BENEFICIARIO');
          localStorage.setItem('userId', '1'); // ID simulado
          this.router.navigate(['/busqueda-ayudas']);
        } else {
          localStorage.setItem('userType', 'ADMINISTRADOR');
          this.router.navigate(['/panel-admin']);
        }
      }, 1000);
    }
  }

  irARegistro(): void {
    this.router.navigate(['/registro-beneficiario']);
  }

  olvidoPassword(): void {
    alert('Funcionalidad de recuperación de contraseña en desarrollo');
  }
}
