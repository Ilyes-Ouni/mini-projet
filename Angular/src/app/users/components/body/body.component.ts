import { Component, Input, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router, ActivatedRoute } from '@angular/router';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';
import { AddFormComponent } from '../add-form/add-form.component';
import { UpdateFormComponent } from '../update-form/update-form.component';
import { UserImageComponent } from '../user-image/user-image.component';
import { environment } from 'src/environments/environment.prod';

@Component({
  selector: 'users-body',
  templateUrl: './body.component.html',
  styleUrls: ['./body.component.css']
})
export class BodyComponent {
  @Input() isAdmin: boolean | undefined;
  url:string = `${environment.apiURL}/image`
  displayedColumns: string[] = ['userID', 'firstname', 'lastname', 'email', 'role', 'image', 'options'];
  dataSource = new MatTableDataSource();
  @ViewChild(MatSort) sort: any;
  @ViewChild(MatPaginator) paginator: any;

  constructor(private userService: UserService, private dialog: MatDialog, private stockDataService: StockDataService) { }

  ngOnInit(): void {
    this.userService.getUsers()
      .subscribe((res: any) => {
        this.dataSource = new MatTableDataSource(res);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator
      })
  }

  deleteUser(userID: number) {
    if (confirm("Êtes-vous sûr(e) de vouloir supprimer ce participant ?")) {
      this.userService.deleteUser(userID)
        .subscribe(
          {
            next:(res: any) => {
              const index = this.dataSource.data.findIndex((user: any) => user.id === userID);
              if (index >= 0) {
                this.dataSource.data.splice(index, 1);
                this.dataSource._updateChangeSubscription();
              }
              Swal.fire({
                icon: 'success',
                title: 'Success...',
                text: 'User has been deleted successfully !',
              })
            },
            error:(res: any)=>{
              const index = this.dataSource.data.findIndex((user: any) => user.id === userID);
              if (index >= 0) {
                this.dataSource.data.splice(index, 1);
                this.dataSource._updateChangeSubscription();
              }
              Swal.fire({
                icon: 'success',
                title: 'Success...',
                text: 'User has been deleted successfully !',
              })
            }
        })
    }
  }

  openImage(image: string){
    this.stockDataService.setIamgePath(image);
    this.dialog.open(UserImageComponent)
  }

  addUser() {
    const dialogRef = this.dialog.open(AddFormComponent);
    dialogRef.afterClosed().subscribe(
      {
        next:() => {
        this.userService.getUsers().subscribe((res: any) => {
          this.dataSource.data = res;
        });
      }
    });
  }

  updateUser(userID: number) {
    this.stockDataService.setID(userID);
    const dialogRef = this.dialog.open(UpdateFormComponent);
    dialogRef.afterClosed().subscribe(
      {
        next:() => {
          this.userService.getUsers().subscribe((res: any) => {
            this.dataSource.data = res;
          });
        }, 
    });
  }
}