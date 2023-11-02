import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from './services/user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private userService: UserService, private router: Router) {}

  async canActivate(): Promise<boolean> {
    let token = localStorage.getItem('token')
    if(token){
      var state: boolean = false;
      await new Promise<void>((resolve) => {
        this.userService.validate()
          .subscribe({
            next: (res: any) => {
              state = res.state;
              console.log(state);
              resolve();
            }
          });
      });
      console.log(state);
      return state;
    } else {
        this.router.navigate(['/auth']);
      return false;
    }
  }
}
