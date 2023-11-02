import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent implements OnInit, OnDestroy {
  roles: Array<string> = [];
  emailExists: boolean = false;
  verificationCode!: string;
  file: File | null = null;
  signupSubscription!: Subscription;
  showVerificationBloc:boolean = false;
  signupForm!: FormGroup;
  verificationForm!: FormGroup;
  constructor(private authService: AuthService, private router: Router, private fb: FormBuilder) {}

  ngOnInit() {
    // Create the signup form
    this.signupForm = this.fb.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
    });

    this.verificationForm = this.fb.group({
      typedCode: ['', Validators.required],
    });
  }


  onFilechange(event: any) {
    this.file = event.target.files[0];
  }

  signup() {
    if(this.signupForm.valid && this.file){
      this.authService.findByEmail(this.signupForm.get('email')?.value)
      .subscribe((res: any)=>{
        if(res != null) {
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'User already exists!',
          });
        }
        else {
          this.showVerificationBloc = true
          this.verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
          const newAccount: object = {
            recipient: this.signupForm.get('email')?.value,
            msgBody: this.verificationCode
          }
          
          this.authService.verifyEmail(newAccount).subscribe({
            next: () =>{
              alert("Check your inbox")
            },
          })
        }
      })
    }
  }


  showCodeBloc(){
    if(this.verificationForm.valid){
      if(this.verificationCode != this.verificationForm.get('typedCode')?.value){
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Wrong validation key!',
        });
      }else{
        const email = this.signupForm.get('email')?.value;
        const firstname = this.signupForm.get('firstname')?.value;
        const lastname = this.signupForm.get('lastname')?.value;
        const password = this.signupForm.get('password')?.value;
  
        const body: object = {
          firstname: firstname,
          lastname: lastname,
          email: email,
          password: password,
          role: 'USER',
        };
        this.signupSubscription = this.authService
          .signup(this.file, body)
          .subscribe({
            next: (res: any) => {
              this.authService.setProfilImage(this.file, email).subscribe({
                next: (res: any) => {
                  Swal.fire({
                    icon: 'success',
                    title: 'Success...',
                    text: 'You has been successfully registered!',
                  });
  
                  const body = { email: email, password: password };
                  this.authService.login(body).subscribe({
                    next: (res: any) => {
                      localStorage.setItem('token', res.token);
                      this.router.navigate(['/regions']);
                    },
                  });
                },
              });
            },
            error: (res: any) => {
              let errorMessage: string = res.error.message;
              if (errorMessage.startsWith('User already exists with email')) {
                Swal.fire({
                  icon: 'error',
                  title: 'Oops...',
                  text: errorMessage,
                });
              } else {
                Swal.fire({
                  icon: 'error',
                  title: 'Oops...',
                  text: 'Please check your information !',
                });
              }
            },
          });
      }
    }
  }

  ngOnDestroy() {
    if (this.signupSubscription) this.signupSubscription.unsubscribe();
  }
}
