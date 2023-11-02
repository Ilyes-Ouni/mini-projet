import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Users } from 'src/app/shared/models/users';
import { environment } from 'src/environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  url:string = environment.apiURL
  constructor(private http: HttpClient) {}
  
  login(body: any){
    return this.http.post(this.url+'/auth/authenticate',  body);
  }

  signup(file: File | null, body: object) {
    return this.http.post(this.url+'/auth/register',  body);
  }

  setProfilImage(file: File | null, userEmail: string) {
    const formData = new FormData();
    formData.append('file', file as File);
    formData.append('userEmail', userEmail);

    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.http.post(this.url+`/upload/user`, formData);
  }

  verifyEmail(body: any){
    return this.http.post(this.url+'/sendMail',  body);
  }

  findByEmail(email: string){
    return this.http.get(this.url+ '/users/findByEmail/' + email);
  }
}