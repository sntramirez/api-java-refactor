import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DonacionService } from '../../services/donacion.service';
import { AsignacionService } from '../../services/asignacion.service';
import { DonacionResponse, TipoDonacion } from '../../models/models';

@Component({
  selector: 'app-busqueda-ayudas',
  templateUrl: './busqueda-ayudas.component.html',
  styleUrls: ['./busqueda-ayudas.component.css']
})
export class BusquedaAyudasComponent implements OnInit {
  ayudasDisponibles: DonacionResponse[] = [];
  ayudasFiltradas: DonacionResponse[] = [];
  tiposDonacion = Object.values(TipoDonacion);

  filtros = {
    tipo: [] as TipoDonacion[],
    ubicacion: '',
    ordenar: 'recientes'
  };

  loading = false;
  beneficiarioId: number = 1; // Simulado, debe venir de autenticación

  constructor(
    private donacionService: DonacionService,
    private asignacionService: AsignacionService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cargarAyudas();
  }

  cargarAyudas(): void {
    this.loading = true;
    this.donacionService.obtenerDisponibles().subscribe({
      next: (ayudas) => {
        this.ayudasDisponibles = ayudas;
        this.ayudasFiltradas = ayudas;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar ayudas:', error);
        this.loading = false;
      }
    });
  }

  aplicarFiltros(): void {
    this.ayudasFiltradas = this.ayudasDisponibles.filter(ayuda => {
      if (this.filtros.tipo.length > 0 && !this.filtros.tipo.includes(ayuda.tipo)) {
        return false;
      }
      return true;
    });

    // Ordenar
    if (this.filtros.ordenar === 'cantidad') {
      this.ayudasFiltradas.sort((a, b) => b.cantidad.localeCompare(a.cantidad));
    }
  }

  toggleFiltroTipo(tipo: TipoDonacion): void {
    const index = this.filtros.tipo.indexOf(tipo);
    if (index > -1) {
      this.filtros.tipo.splice(index, 1);
    } else {
      this.filtros.tipo.push(tipo);
    }
    this.aplicarFiltros();
  }

  solicitarAyuda(ayuda: DonacionResponse): void {
    if (confirm(`¿Deseas solicitar esta ayuda de ${ayuda.tipo}?`)) {
      this.asignacionService.crearAsignacion(ayuda.id, this.beneficiarioId).subscribe({
        next: (asignacion) => {
          alert('Solicitud enviada exitosamente');
          this.cargarAyudas();
          this.router.navigate(['/mis-solicitudes']);
        },
        error: (error) => {
          console.error('Error al solicitar ayuda:', error);
          alert('Error al enviar la solicitud');
        }
      });
    }
  }

  cerrarSesion(): void {
    localStorage.clear();
    this.router.navigate(['/home']);
  }
}
