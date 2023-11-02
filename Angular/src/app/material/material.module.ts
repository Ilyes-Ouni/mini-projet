import { NgModule } from '@angular/core';
import {MatIconModule} from '@angular/material/icon'
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatListModule} from '@angular/material/list';
import {MatButtonModule } from "@angular/material/button";
import {MatMenuModule} from '@angular/material/menu';
import {MatTabsModule} from '@angular/material/tabs';
import {MatTableModule} from '@angular/material/table';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatDatepickerModule } from '@angular/material/datepicker';
import {MatNativeDateModule } from "@angular/material/core";
import {MatDialogModule } from '@angular/material/dialog';
import {MatPaginatorModule } from "@angular/material/paginator";
import {HttpClientModule } from "@angular/common/http";
// import {BrowserAnimationsModule } from "@angular/platform-browser/animations";
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatCardModule} from '@angular/material/card';
import { MatSortModule } from "@angular/material/sort";
import {MatSelectModule} from '@angular/material/select';
import {MatRadioModule} from '@angular/material/radio';

const materialComponents = [
  MatIconModule,
  MatToolbarModule,
  MatListModule,
  MatButtonModule,
  MatMenuModule,
  MatTabsModule,
  MatTableModule,
  MatInputModule,
  MatFormFieldModule,
  MatDatepickerModule,
  MatNativeDateModule,
  MatDialogModule,
  MatPaginatorModule,
  HttpClientModule,
  MatCardModule,
  MatSortModule,
  MatSelectModule,
  MatRadioModule,
  MatSidenavModule
]

@NgModule({
  declarations: [],
  imports: [materialComponents],
  exports: [materialComponents],
})
export class MaterialModule { }
