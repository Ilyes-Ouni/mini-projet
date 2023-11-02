import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RegionsRoutingModule } from './regions-routing.module';
import { InterfaceComponent } from './components/interface/interface.component';
import { SharedModule } from '../shared/shared.module';
import { BodyComponent } from './components/body/body.component';
import { MaterialModule } from '../material/material.module';
import { AddFormComponent } from './components/add-form/add-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UpdateFormComponent } from './components/update-form/update-form.component';
import { RegionService } from './services/region.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../shared/http-interceptor';


@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    MaterialModule,
    RegionsRoutingModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  declarations: [
    InterfaceComponent,
    BodyComponent,
    AddFormComponent,
    UpdateFormComponent
  ],
  providers: [
    RegionService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
})
export class RegionsModule { }
