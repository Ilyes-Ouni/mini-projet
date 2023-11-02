import { Component, Input, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import Swal from 'sweetalert2';
import { RegionService } from '../../services/region.service';
import { AddFormComponent } from '../add-form/add-form.component';
import { UpdateFormComponent } from '../update-form/update-form.component';

@Component({
  selector: 'regions-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.css']
})
export class BodyComponent {
  @Input() isAdmin: boolean | undefined;
  displayedColumns: string[] = ['idRegion', 'nom', 'dateCreation', 'options'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort) sort: any;
  @ViewChild(MatPaginator) paginator: any;

  constructor(private regionService: RegionService, private dialog: MatDialog, private stockDataService: StockDataService) { }

  ngOnInit(): void {
    this.regionService.getRegions()
      .subscribe((res: any) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator
      })
  }

  deleteRegion(regionID: number) {
    if (confirm("Êtes-vous sûr(e) de vouloir supprimer ce participant ?")) {
      this.regionService.deleteRegion(regionID)
        .subscribe(
          {
            next:(res: any) => {
              const index = this.dataSource.data.findIndex((region: any) => region.idRegion === regionID);
              if (index >= 0) {
                this.dataSource.data.splice(index, 1);
                this.dataSource._updateChangeSubscription();
              }
              Swal.fire({
                icon: 'success',
                title: 'Success...',
                text: 'Region has been deleted successfully !',
              })
            },
            error:(res: any)=>{
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'There are clients related to this region, you should delete the clients first !',
              })
            }
        })
    }
  }

  addRegion() {
    const dialogRef = this.dialog.open(AddFormComponent);
    dialogRef.afterClosed().subscribe(
      {
        next:() => {
        this.regionService.getRegions().subscribe((res: any) => {
          this.dataSource.data = res;
        });
      }
    });
  }

  updateRegion(regionID: number) {
    this.stockDataService.setID(regionID);
    const dialogRef = this.dialog.open(UpdateFormComponent);
    dialogRef.afterClosed().subscribe(
      {
        next:() => {
          this.regionService.getRegions().subscribe((res: any) => {
            this.dataSource.data = res;
          });
        }, 
    });
  }
}