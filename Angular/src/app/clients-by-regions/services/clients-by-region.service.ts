import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.prod';
import { AuthInterceptor } from 'src/app/shared/http-interceptor';

@Injectable({
  providedIn: 'root'
})
export class ClientsByRegionService {
  url: string=environment.apiURL;
  constructor(private http: HttpClient) {}
  
  getRegions(){
    return this.http.get(`${this.url}/regions`);
  } 

  getClientsByRegion(selectedRegion: string) {
    return this.http.get(`${this.url}/regions/clients-by-region/${selectedRegion}`);
  }
}
