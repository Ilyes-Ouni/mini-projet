import { Component, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { ClientsByRegionService } from '../../services/clients-by-region.service';

@Component({
  selector: 'clientsByRegion-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.css']
})
export class BodyComponent {
  selectedRegion!: number;
  regions: Array<any> = [];
  clients: Array<any> = [];
  getRegionsSubscription!: Subscription;
  displayedColumns: string[] = ['idClient', 'nom', 'email', 'phoneNumber', 'dateNaissance', 'dateCreation', 'region'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort) sort: any;
  @ViewChild(MatPaginator) paginator: any;

  constructor(private clientByRegionService: ClientsByRegionService) { }

  ngOnInit(): void {
    this.clientByRegionService.getRegions()
      .subscribe((res: any) => {
        this.regions = res
        this.selectedRegion = res[0].idRegion;

        this.getRegionsSubscription =
        this.clientByRegionService.getClientsByRegion(String(this.selectedRegion))
        .subscribe({
          next:(res: any)=>{
            this.dataSource = new MatTableDataSource(res);
            this.dataSource.sort = this.sort;
            this.dataSource.paginator = this.paginator
          }
        })
      })
  }



  onSelectregion(selectedRegion: string) {
    this.selectedRegion = Number(selectedRegion);
    this.getRegionsSubscription =
     this.clientByRegionService.getClientsByRegion(selectedRegion)
      .subscribe({
        next: (res: any) => {
          this.clients = res.participants
          this.dataSource.data = res;
        }
      })
  }
}
