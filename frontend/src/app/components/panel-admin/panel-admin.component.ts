import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DonacionService } from '../../services/donacion.service';
import { BeneficiarioService } from '../../services/beneficiario.service';
import { AsignacionService } from '../../services/asignacion.service';
import { EstadoDonacion, EstadoAsignacion } from '../../models/models';

@Component({
  selector: 'app-panel-admin',
  templateUrl: './panel-admin.component.html',
  styleUrls: ['./panel-admin.component.css']
})
export class PanelAdminComponent implements OnInit {
  seccionActiva: string = 'dashboard';

  metricas = {
    totalDonaciones: 0,
    totalBeneficiarios: 0,
    ayudasPendientes: 0,
    ayudasEnTransito: 0
  };

  donaciones: any[] = [];
  beneficiarios: any[] = [];
  asignaciones: any[] = [];

  constructor(
    private donacionService: DonacionService,
    private beneficiarioService: BeneficiarioService,
    private asignacionService: AsignacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarMetricas();
  }

  cambiarSeccion(seccion: string): void {
    this.seccionActiva = seccion;

    switch(seccion) {
      case 'donaciones':
        this.cargarDonaciones();
        break;
      case 'beneficiarios':
        this.cargarBeneficiarios();
        break;
      case 'asignaciones':
        this.cargarAsignaciones();
        break;
    }
  }

  cargarMetricas(): void {
    // Simular carga de métricas
    this.metricas = {
      totalDonaciones: 150,
      totalBeneficiarios: 85,
      ayudasPendientes: 12,
      ayudasEnTransito: 8
    };
  }

  cargarDonaciones(): void {
    this.donacionService.obtenerPorEstado(EstadoDonacion.NUEVA).subscribe({
      next: (donaciones) => {
        this.donaciones = donaciones;
      },
      error: (error) => console.error('Error:', error)
    });
  }

  cargarBeneficiarios(): void {
    this.beneficiarioService.obtenerTodos().subscribe({
      next: (beneficiarios) => {
        this.beneficiarios = beneficiarios;
      },
      error: (error) => console.error('Error:', error)
    });
  }

  cargarAsignaciones(): void {
    this.asignacionService.obtenerPorEstado(EstadoAsignacion.ASIGNADA).subscribe({
      next: (asignaciones) => {
        this.asignaciones = asignaciones;
      },
      error: (error) => console.error('Error:', error)
    });
  }

  cerrarSesion(): void {
    localStorage.clear();
    this.router.navigate(['/home']);
  }
}
