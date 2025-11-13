import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BeneficiarioRegistroRequest, BeneficiarioResponse } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class BeneficiarioService {
  private apiUrl = 'http://localhost:8080/api/beneficiarios';

  constructor(private http: HttpClient) {}

  registrar(request: BeneficiarioRegistroRequest): Observable<BeneficiarioResponse> {
    return this.http.post<BeneficiarioResponse>(`${this.apiUrl}/registro`, request);
  }

  obtenerTodos(): Observable<BeneficiarioResponse[]> {
    return this.http.get<BeneficiarioResponse[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<BeneficiarioResponse> {
    return this.http.get<BeneficiarioResponse>(`${this.apiUrl}/${id}`);
  }
}
