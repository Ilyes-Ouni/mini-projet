import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import jwt_decode from 'jwt-decode';
import { environment } from 'src/environments/environment.prod';
import { StockDataService } from '../../services/stock-data.service';
import { UserService } from '../../services/user.service';
import { ImageUploaderComponent } from '../image-uploader/image-uploader.component';

@Component({
  selector: 'shared-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit{
  constructor(private dialog: MatDialog, private stockDataService: StockDataService, private userService: UserService){}
  
  avatar:string = `${environment.apiURL}/image/`;
  username!: string;
  email!: string;
  bar = false;
  menu = true;

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    const decodedToken: any = jwt_decode(token);
    
    this.username = decodedToken.nom
    this.avatar += decodedToken.imagePath
    this.email = decodedToken.sub
  }

  toggleMenu() {
    this.menu = !this.menu;
  }

  toggleBar() {
    this.bar = !this.bar;
  }

  updateImageDialog(){
    this.stockDataService.setIamgePath(this.avatar);
    const dialogRef = this.dialog.open(ImageUploaderComponent);

    dialogRef.afterClosed().subscribe(result => {
       this.avatar = `${environment.apiURL}/image/${ result.imagePath }`
    });
  }

  logout(){
    localStorage.removeItem('token');
  }
}
