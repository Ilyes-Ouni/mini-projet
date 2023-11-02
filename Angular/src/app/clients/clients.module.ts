import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClientsRoutingModule } from './clients-routing.module';
import { InterfaceComponent } from './components/interface/interface.component';
import { SharedModule } from '../shared/shared.module';
import { BodyComponent } from './components/body/body.component';
import { MaterialModule } from '../material/material.module';
import { AddFormComponent } from './components/add-form/add-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UpdateFormComponent } from './components/update-form/update-form.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../shared/http-interceptor';
import { ClientService } from './services/client.service';
import { ClientImageComponent } from './components/client-image/client-image.component';


@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MaterialModule,
    ClientsRoutingModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  declarations: [
    InterfaceComponent,
    BodyComponent,
    AddFormComponent,
    UpdateFormComponent,
    ClientImageComponent,
  ],
  providers: [
    ClientService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class ClientsModule { }
