import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Region } from '../models/region';

@Injectable({
  providedIn: 'root'
})
export class RegionService {
  
  url: string=environment.apiUrl;
  
  constructor(private http: HttpClient) { }

  getRegions(){
    return this.http.get(`${this.url}/regions`);
  } 

  getRegion(regionID: number){
    return this.http.get(`${this.url}/regions/${regionID}`);
  } 
  
  addRegion(region: Region){
    return this.http.post(`${this.url}/regions`, region);
  }
  
  updateRegion(regionID: number, nom: string) {
    return this.http.put(`${this.url}/regions/${regionID}`, {nom});
  }

  deleteRegion(regionID: number){
    return this.http.delete(`${this.url}/regions/${regionID}`);
  }
}
