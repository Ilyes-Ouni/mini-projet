import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/shared/services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  email!: string;
  password!: string;

  constructor(private authService: AuthService, private userService: UserService, private router: Router){}

  ngOnInit() {
    let token = localStorage.getItem('token')
    if(token){
      this.userService.validate()
      .subscribe({
        next:(res: any)=>{
          this.router.navigate(['/regions']);
        }
      })
    }
  }


  login(){
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (emailRegex.test(this.email) && this.password){
      const body = {email: this.email, password: this.password}
      this.authService.login(body)
      .subscribe(
        {
          next:(res: any)=>{
            localStorage.setItem('token', res.token)
            this.router.navigate(['/regions']);
          }
        }
      )
    }
  }


}
