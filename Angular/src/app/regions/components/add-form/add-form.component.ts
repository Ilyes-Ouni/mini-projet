import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { Region } from '../../models/region';
import { RegionService } from '../../services/region.service';

@Component({
  selector: 'app-add-form',
  templateUrl: './add-form.component.html',
  styleUrls: ['./add-form.component.css']
})
export class AddFormComponent {
  newRegion= new Region();
  regionName!: string;
  constructor(private regionService: RegionService, private dialog: MatDialog) { }

  addRegion(){
    this.newRegion.nom = this.regionName
    this.regionService.addRegion(this.newRegion)
    .subscribe({
      next:(res: any)=>{
        this.dialog.closeAll();
        Swal.fire({
          icon: 'success',
          title: 'Success...',
          text: 'Region has been added successfully !',
        })
    },
    error:(res: any)=>{
      Swal.fire({
        icon: 'error',
        title: 'Oops...',
        text: 'Could not add this region !',
      })
    }
  })
  }
}
