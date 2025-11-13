import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Components
import { HomeComponent } from './components/home/home.component';
import { ConfirmacionComponent } from './components/confirmacion/confirmacion.component';
import { RegistroBeneficiarioComponent } from './components/registro-beneficiario/registro-beneficiario.component';
import { LoginComponent } from './components/login/login.component';
import { BusquedaAyudasComponent } from './components/busqueda-ayudas/busqueda-ayudas.component';
import { MisSolicitudesComponent } from './components/mis-solicitudes/mis-solicitudes.component';
import { PanelAdminComponent } from './components/panel-admin/panel-admin.component';

// Services
import { DonacionService } from './services/donacion.service';
import { BeneficiarioService } from './services/beneficiario.service';
import { AsignacionService } from './services/asignacion.service';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ConfirmacionComponent,
    RegistroBeneficiarioComponent,
    LoginComponent,
    BusquedaAyudasComponent,
    MisSolicitudesComponent,
    PanelAdminComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    DonacionService,
    BeneficiarioService,
    AsignacionService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
