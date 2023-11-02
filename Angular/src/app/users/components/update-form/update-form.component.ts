import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Authority, Users } from '../../../shared/models/users';

@Component({
  selector: 'app-update-form',
  templateUrl: './update-form.component.html',
  styleUrls: ['./update-form.component.css']
})
export class UpdateFormComponent implements OnInit{
  oldImagePath!: string;
  file: File | null = null;
  userID!: number;
  user:Users = new Users()  

  conferencesSubscription!: Subscription;
  addParticipantSubscription!: Subscription;
  constructor(private userService: UserService, private fb: FormBuilder, private stockDataService: StockDataService,
    private route: ActivatedRoute, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.stockDataService.id.subscribe((res: any) => {
      this.userID = res
    })

    this.userService.getUser(this.userID)
      .subscribe(
        {
          next: (res: any) => {
            console.log(res);
            this.oldImagePath = res.imagePath
            this.userID = res.id
            this.user = res
            this.UserForm.setValue({
              Firstname: res.firstname,
              email: res.email,
              Lastname: res.lastname,
              Authority: res.role,
            })
          }
        }
      )
  }

  UserForm = this.fb.group({
    Firstname: [, Validators.required],
    Lastname: [, Validators.required],
    email: [, Validators.required],
    Authority: [, Validators.required],
  })


  UpdateUs(form: any) {
    if (this.UserForm.status == 'VALID') {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (emailRegex.test(form.email)) {       
        this.user.email = form.email
        this.user.firstname = form.Firstname
        this.user.lastname = form.Lastname
        this.user.role = form.Authority
        this.user.authorities = []; // Empty the authorities array
        this.user.authorities.push({ authority: form.Authority }); // Push the form.Authority


        if(!this.file) form.imagePath = this.oldImagePath
        this.addParticipantSubscription = this.userService.updateUser(this.userID, this.user)
          .subscribe({
            next: (res: any) => {
              if(this.file){

                this.userService.setProfilImage(this.file, form.email)
                  .subscribe({
                    next: (res) => {
                      this.dialog.closeAll();
                      Swal.fire({
                        icon: 'success',
                        title: 'Success...',
                        text: 'User has been updated successfully !',
                      })
                    },
                    error: (res: any) => {
                      Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Could not update this User !',
                      })
                    }
                  })
                }else{
                  this.dialog.closeAll();
                  Swal.fire({
                    icon: 'success',
                    title: 'Success...',
                    text: 'Client has been updated successfully !',
                  })
                }
            }
          })
      }
    }
  }

  onFilechange(event: any) {
    this.file = event.target.files[0]
  }

  ngOnDestroy() {
    if (this.conferencesSubscription) this.conferencesSubscription.unsubscribe();
    if (this.addParticipantSubscription) this.addParticipantSubscription.unsubscribe();
  }
}
