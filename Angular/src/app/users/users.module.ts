import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UsersRoutingModule } from './users-routing.module';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../material/material.module';
import { AuthInterceptor } from '../shared/http-interceptor';
import { SharedModule } from '../shared/shared.module';
import { AddFormComponent } from './components/add-form/add-form.component';
import { BodyComponent } from './components/body/body.component';
import { InterfaceComponent } from './components/interface/interface.component';
import { UpdateFormComponent } from './components/update-form/update-form.component';
import { UserService } from './services/user.service';


@NgModule({
    imports: [
      CommonModule,
      SharedModule,
      MaterialModule,
      UsersRoutingModule,
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
      UserService,
      {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthInterceptor,
        multi: true
      }
    ]
})
export class UsersModule { }
