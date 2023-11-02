import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AuthInterceptor } from 'src/app/shared/http-interceptor';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  url: string=environment.apiUrl;
  constructor(private http: HttpClient) { }
  
  getClients(){
    return this.http.get(`${this.url}/clients`);
  } 
  
  getClient(clientID: number){
    return this.http.get(`${this.url}/clients/${clientID}`);
  } 
  
  updateClient(clientID: number, regionID: number, body: any) {
    return this.http.put(`${this.url}/clients/${clientID}/${regionID}`, body);
  }
  
  deleteClient(clientID: number){
    return this.http.delete(`${this.url}/clients/${clientID}`);
  }

  addClient(regionID: number, body: any){
    return this.http.post(`${this.url}/regions/${regionID}/clients`, body);
  }

  setClientImage(file: File | null, clientID: number | null) {
    
    const formData = new FormData();
    console.log(formData);
    formData.append('file', file as File);
    formData.append('clientID', String(clientID));

    
    
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');
    return this.http.post(this.url+`/upload/client`, formData);
  }
}
