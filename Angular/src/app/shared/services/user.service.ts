import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  url:string = environment.apiURL
  constructor(private http: HttpClient, private router: Router) {}

  validate(){
    return this.http.post(this.url+'/auth/validateToken', null, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')?.toString()}`}});
  }

  updateUser(body: any){
    return this.http.put(this.url+'/users/id',  body, {headers: {'Authorization': `Bearer ${localStorage.getItem('token')?.toString()}`}});
  }

  getUser(body: any){
    return this.http.get(this.url+'/users/id',  {headers: {'Authorization': `Bearer ${localStorage.getItem('token')?.toString()}`}});
  }

  setUserImage(file: File | null, userEmail: string | null) {
    const formData = new FormData();
    formData.append('file', file as File);
    formData.append('userEmail', String(userEmail));

    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.http.post(this.url+`/upload/user`, formData);
  }
}
