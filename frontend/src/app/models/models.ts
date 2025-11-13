export enum TipoDonacion {
  COMIDA = 'COMIDA',
  DINERO = 'DINERO',
  MEDICAMENTOS = 'MEDICAMENTOS',
  ROPA = 'ROPA',
  OTRO = 'OTRO'
}

export enum EstadoDonacion {
  NUEVA = 'NUEVA',
  REVISADA = 'REVISADA',
  ASIGNADA = 'ASIGNADA',
  ENTREGADA = 'ENTREGADA'
}

export enum EstadoSolicitud {
  PENDIENTE = 'PENDIENTE',
  ACEPTADA = 'ACEPTADA',
  EN_CAMINO = 'EN_CAMINO',
  ENTREGADA = 'ENTREGADA',
  RECHAZADA = 'RECHAZADA'
}

export enum EstadoAsignacion {
  ASIGNADA = 'ASIGNADA',
  EN_CAMINO = 'EN_CAMINO',
  ENTREGADA = 'ENTREGADA',
  CANCELADA = 'CANCELADA'
}

export interface DonacionRequest {
  tipo: TipoDonacion;
  cantidad: string;
  descripcion?: string;
  nombreDonante: string;
  telefonoDonante: string;
  emailDonante: string;
  comentarios?: string;
}

export interface DonacionResponse {
  id: number;
  tipo: TipoDonacion;
  cantidad: string;
  descripcion?: string;
  nombreDonante: string;
  estado: EstadoDonacion;
  fechaDonacion: string;
  numeroReferencia: string;
}

export interface BeneficiarioRegistroRequest {
  nombre: string;
  cedula: string;
  fechaNacimiento: string;
  telefono: string;
  email?: string;
  direccion: string;
  ciudad: string;
  descripcionSituacion?: string;
  ayudasNecesarias: TipoDonacion[];
  personasEnHogar: number;
  password: string;
}

export interface BeneficiarioResponse {
  id: number;
  nombre: string;
  cedula: string;
  fechaNacimiento: string;
  telefono: string;
  email?: string;
  direccion: string;
  ciudad: string;
  descripcionSituacion?: string;
  ayudasNecesarias: TipoDonacion[];
  personasEnHogar: number;
  activo: boolean;
}

export interface AsignacionResponse {
  id: number;
  donacion: DonacionResponse;
  beneficiario: BeneficiarioResponse;
  estado: EstadoAsignacion;
  fechaAsignacion: string;
  fechaActualizacion: string;
  comentarios?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
  tipoUsuario: 'BENEFICIARIO' | 'ADMINISTRADOR';
}
