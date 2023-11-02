import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InterfaceComponent } from './components/interface/interface.component';
import { BodyComponent } from './components/body/body.component';
import { MaterialModule } from '../material/material.module';
import { SharedModule } from '../shared/shared.module';
import { ClientsRoutingModule } from './clients-by-region-routing.module';
import { ClientsByRegionService } from './services/clients-by-region.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../shared/http-interceptor';



@NgModule({
  imports: [
    CommonModule,
    MaterialModule,
    SharedModule,
    ClientsRoutingModule,
  ],
  declarations: [
    InterfaceComponent,
    BodyComponent
  ],
  providers: [
    ClientsByRegionService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class ClientsByRegionsModule { }
