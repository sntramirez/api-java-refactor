import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AsignacionResponse, EstadoAsignacion } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class AsignacionService {
  private apiUrl = 'http://localhost:8080/api/asignaciones';

  constructor(private http: HttpClient) {}

  crearAsignacion(donacionId: number, beneficiarioId: number, comentarios?: string): Observable<AsignacionResponse> {
    return this.http.post<AsignacionResponse>(this.apiUrl, {
      donacionId,
      beneficiarioId,
      comentarios
    });
  }

  obtenerPorBeneficiario(beneficiarioId: number): Observable<AsignacionResponse[]> {
    return this.http.get<AsignacionResponse[]>(`${this.apiUrl}/beneficiario/${beneficiarioId}`);
  }

  obtenerPorEstado(estado: EstadoAsignacion): Observable<AsignacionResponse[]> {
    return this.http.get<AsignacionResponse[]>(`${this.apiUrl}/estado/${estado}`);
  }

  actualizarEstado(id: number, estado: EstadoAsignacion, comentarios?: string): Observable<AsignacionResponse> {
    return this.http.put<AsignacionResponse>(`${this.apiUrl}/${id}/estado`, {
      estado,
      comentarios
    });
  }
}
