import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AsignacionService } from '../../services/asignacion.service';
import { AsignacionResponse, EstadoAsignacion } from '../../models/models';

@Component({
  selector: 'app-mis-solicitudes',
  templateUrl: './mis-solicitudes.component.html',
  styleUrls: ['./mis-solicitudes.component.css']
})
export class MisSolicitudesComponent implements OnInit {
  solicitudes: AsignacionResponse[] = [];
  solicitudesFiltradas: AsignacionResponse[] = [];
  filtroEstado: string = 'TODAS';
  loading = false;
  beneficiarioId: number = 1; // Simulado

  constructor(
    private asignacionService: AsignacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarSolicitudes();
  }

  cargarSolicitudes(): void {
    this.loading = true;
    this.asignacionService.obtenerPorBeneficiario(this.beneficiarioId).subscribe({
      next: (solicitudes) => {
        this.solicitudes = solicitudes;
        this.aplicarFiltro();
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar solicitudes:', error);
        this.loading = false;
      }
    });
  }

  aplicarFiltro(): void {
    if (this.filtroEstado === 'TODAS') {
      this.solicitudesFiltradas = this.solicitudes;
    } else {
      this.solicitudesFiltradas = this.solicitudes.filter(
        s => s.estado === this.filtroEstado
      );
    }
  }

  marcarComoRecibida(solicitud: AsignacionResponse): void {
    if (confirm('¿Confirmas que has recibido esta ayuda?')) {
      this.asignacionService.actualizarEstado(
        solicitud.id,
        EstadoAsignacion.ENTREGADA,
        'Confirmado por beneficiario'
      ).subscribe({
        next: () => {
          alert('Ayuda marcada como recibida');
          this.cargarSolicitudes();
        },
        error: (error) => {
          console.error('Error:', error);
          alert('Error al actualizar estado');
        }
      });
    }
  }

  cerrarSesion(): void {
    localStorage.clear();
    this.router.navigate(['/home']);
  }
}
