import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import Swal from 'sweetalert2';
import { RegionService } from '../../services/region.service';

@Component({
  selector: 'app-update-form',
  templateUrl: './update-form.component.html',
  styleUrls: ['./update-form.component.css']
})
export class UpdateFormComponent implements OnInit{
  regionID!: number;
  regionName!: string;
  constructor(private stockDataService: StockDataService, private regionService: RegionService, private dialog: MatDialog){}

  ngOnInit(): void {
    this.stockDataService.id.subscribe(res=>{
      this.regionID = res;
    }).unsubscribe()

    this.regionService.getRegion(this.regionID)
    .subscribe((res: any)=>{
      this.regionName = res.nom
    })
  }

  updateRegion(){
    if(this.regionName && this.regionName.length > 0){
      this.regionService.updateRegion(this.regionID, this.regionName)
      .subscribe(
        {
          next:(res)=>{
            this.dialog.closeAll();
            Swal.fire({
              icon: 'success',
              title: 'Success...',
              text: 'Region has been updated successfully !',
            })
          },
          error:(err)=>{
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'Could not update this region !',
            })
          }
      } 
      )
    }
  }
}
