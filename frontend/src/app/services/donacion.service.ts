import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DonacionRequest, DonacionResponse, EstadoDonacion } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class DonacionService {
  private apiUrl = 'http://localhost:8080/api/donaciones';

  constructor(private http: HttpClient) {}

  crearDonacion(request: DonacionRequest): Observable<DonacionResponse> {
    return this.http.post<DonacionResponse>(this.apiUrl, request);
  }

  obtenerPorEstado(estado: EstadoDonacion): Observable<DonacionResponse[]> {
    return this.http.get<DonacionResponse[]>(`${this.apiUrl}/estado/${estado}`);
  }

  obtenerDisponibles(): Observable<DonacionResponse[]> {
    return this.http.get<DonacionResponse[]>(`${this.apiUrl}/disponibles`);
  }

  actualizarEstado(id: number, estado: EstadoDonacion): Observable<DonacionResponse> {
    return this.http.put<DonacionResponse>(`${this.apiUrl}/${id}/estado`, { estado });
  }
}
