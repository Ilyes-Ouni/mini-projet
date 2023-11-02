import { Component, Input, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import { environment } from 'src/environments/environment.prod';
import Swal from 'sweetalert2';
import { ClientService } from '../../services/client.service';
import { AddFormComponent } from '../add-form/add-form.component';
import { ClientImageComponent } from '../client-image/client-image.component';
import { UpdateFormComponent } from '../update-form/update-form.component';

@Component({
  selector: 'clients-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.css']
})
export class BodyComponent {
  @Input() isAdmin: boolean | undefined;
  url:string = `${environment.apiURL}/image`
  displayedColumns: string[] = ['idClient', 'nom', 'email', 'phoneNumber', 'dateNaissance', 'dateCreation', 'region', 'image', 'options'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort) sort: any;
  @ViewChild(MatPaginator) paginator: any;

  constructor(private clientService: ClientService, private router: Router, private route: ActivatedRoute,
    private dialog: MatDialog, private stockDataService: StockDataService) { }

  ngOnInit(): void {
    this.clientService.getClients()
      .subscribe((res: any) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator
      })
  }

  openImage(image: string){
    this.stockDataService.setIamgePath(image);
    this.dialog.open(ClientImageComponent)
  }

  deleteClient(clientID: number) {
    if (confirm("Êtes-vous sûr de vouloir supprimer ce participant?")) {
      this.clientService.deleteClient(clientID)
        .subscribe(
          {
            next: (res: any) => {
              const index = this.dataSource.data.findIndex((client: any) => client.idClient === clientID);
              if (index >= 0) {
                this.dataSource.data.splice(index, 1);
                this.dataSource._updateChangeSubscription();
              }
            }
          }
        )
    }
  }

  addClient() {
    const dialogRef = this.dialog.open(AddFormComponent)
    dialogRef.afterClosed().subscribe(
      {
        next:() => {
        this.clientService.getClients().subscribe((res: any) => {
          this.dataSource.data = res;
        });
      },
      error:(res: any)=>{
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Could not add this client !',
        })
      }
    });
  }

  updateClient(clientID: number) {
    this.stockDataService.setID(clientID)
    const dialogRef = this.dialog.open(UpdateFormComponent)
    dialogRef.afterClosed().subscribe(
      {
        next:() => {
          this.clientService.getClients().subscribe((res: any) => {
            this.dataSource.data = res;
          });
        }, 
    });
  }
}