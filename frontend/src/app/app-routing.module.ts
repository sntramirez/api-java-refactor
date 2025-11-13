import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './components/home/home.component';
import { ConfirmacionComponent } from './components/confirmacion/confirmacion.component';
import { RegistroBeneficiarioComponent } from './components/registro-beneficiario/registro-beneficiario.component';
import { LoginComponent } from './components/login/login.component';
import { BusquedaAyudasComponent } from './components/busqueda-ayudas/busqueda-ayudas.component';
import { MisSolicitudesComponent } from './components/mis-solicitudes/mis-solicitudes.component';
import { PanelAdminComponent } from './components/panel-admin/panel-admin.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'confirmacion', component: ConfirmacionComponent },
  { path: 'registro-beneficiario', component: RegistroBeneficiarioComponent },
  { path: 'login', component: LoginComponent },
  { path: 'busqueda-ayudas', component: BusquedaAyudasComponent },
  { path: 'mis-solicitudes', component: MisSolicitudesComponent },
  { path: 'panel-admin', component: PanelAdminComponent },
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
