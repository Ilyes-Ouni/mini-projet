import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Users } from '../../shared/models/users';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  url: string=environment.apiUrl;
  
  constructor(private http: HttpClient) { }

  getUsers(){
    return this.http.get(`${this.url}/users`);
  } 

  getUser(regionID: number){
    return this.http.get(`${this.url}/users/${regionID}`);
  } 
  
  updateUser(userID: number, user: Users) {   
    return this.http.put(`${this.url}/users/${userID}`, user);
  }

  deleteUser(userID: number){
    return this.http.delete(`${this.url}/users/${userID}`);
  }

  setProfilImage(file: File | null, userEmail: string) {
    const formData = new FormData();
    formData.append('file', file as File);
    formData.append('userEmail', userEmail);

    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.http.post(this.url+`/upload/user`, formData);
  }
}
