import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { NavComponent } from './components/nav/nav.component';
import { MaterialModule } from '../material/material.module';
import { ImageUploaderComponent } from './components/image-uploader/image-uploader.component';


@NgModule({
  imports: [
    RouterModule,
    CommonModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  declarations: [
    NavComponent,
    ImageUploaderComponent
  ],
  exports: [
    NavComponent
  ]
})
export class SharedModule { }
