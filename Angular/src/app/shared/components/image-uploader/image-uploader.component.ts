import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { environment } from 'src/environments/environment.prod';
import Swal from 'sweetalert2';
import { StockDataService } from '../../services/stock-data.service';
import { UserService } from '../../services/user.service';
import jwt_decode from 'jwt-decode';
import { NavComponent } from '../nav/nav.component';

@Component({
  selector: 'app-image-uploader',
  templateUrl: './image-uploader.component.html',
  styleUrls: ['./image-uploader.component.css']
})
export class ImageUploaderComponent implements OnInit{
  imagePath: string = '';
  file: null | File= null;
  constructor(private stockDataService: StockDataService, private userService: UserService, private dialog: MatDialog,
    private dialogRef: MatDialogRef<NavComponent>){}

  ngOnInit() {
    this.stockDataService.image.subscribe(res=>{
      this.imagePath = res;
    })
  }

  uploadImage(){   
    if(this.file){
      const token = localStorage.getItem('token');
      const decodedToken: any = jwt_decode(token);
      const email = decodedToken.sub;

      this.userService.setUserImage(this.file, email)
      .subscribe({
          next: (res: any) => {
            this.dialog.closeAll();
            this.closeDialog(res.filename)
            // localStorage.setItem('token', "eyJzdWIiOiJpbHllRHNAZ21haWwuY29tIiwicm9sZSI6IkFETUlOIiwiaW1hZ2VQYXRoIjoiMTY4NDA3NTA2MjEwNS5wbmciLCJub20iOiJpbHllcyBvdW5pIiwiaWF0IjoxNjg0MDc1MDYyLCJleHAiOjE2ODQyMTc2MjJ9")
            Swal.fire({
              icon: 'success',
              title: 'Success...',
              text: 'Profil Picture has been updated successfully !',
            })
          },
          error: (res: any) => {
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'Could not update the profil picture !',
            })
          }
      })
    }
  }

  onFilechange(event: any) {
    this.file = event.target.files[0]
  }


  closeDialog(imagePath: string): void {
    this.dialogRef.close({ imagePath });
  }
}

