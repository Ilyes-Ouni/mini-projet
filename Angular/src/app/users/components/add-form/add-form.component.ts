import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { Users } from '../../../shared/models/users';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/authentication/services/auth.service';

@Component({
  selector: 'app-add-form',
  templateUrl: './add-form.component.html',
  styleUrls: ['./add-form.component.css']
})
export class AddFormComponent implements OnInit, OnDestroy {
  roles: Array<string> = [];
  email!: string;
  password!: string;
  firstname!: string;
  lastname!: string;
  confirmPassword!: string;
  file: File | null = null;
  signupSubscription!: Subscription
  
  ngOnInit(){}

  constructor(private authService: AuthService, private router: Router){}

  onFilechange(event: any) {
    this.file = event.target.files[0]
  }

  signup(){
    if (this.password && this.password === this.confirmPassword) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if(this.firstname && this.lastname && emailRegex.test(this.email) && this.file){
        const body: object = {
                                firstname: this.firstname,
                                lastname: this.lastname,
                                email: this.email,
                                password: this.password,
                                role: "USER",
                             }
          this.signupSubscription = this.authService.signup(this.file, body)
          .subscribe(
            {
              next:(res: any)=>{

                this.authService.setProfilImage(this.file, this.email)
                .subscribe(
                  {
                    next:(res: any)=>{
                      Swal.fire({
                        icon: 'success',
                        title: 'Success...',
                        text: 'You has been successfully registered!',
                      })
                      this.router.navigate(['/auth']);
                    }
                  }
                )
              },
              error:(res: any)=>{

                let errorMessage: string = res.error.message;
                if(errorMessage.startsWith("User already exists with email")){
                  Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: errorMessage,
                  })                  
                }else{
                  Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Please check your information !',
                  })
                }
              }
            }
          )
      }
    }
  }

  ngOnDestroy() {
    if (this.signupSubscription) this.signupSubscription.unsubscribe();
  }
}
