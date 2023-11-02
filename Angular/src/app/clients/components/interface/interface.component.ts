import { Component, OnDestroy, OnInit } from '@angular/core';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-interface',
  templateUrl: './interface.component.html',
  styleUrls: ['./interface.component.css']
})
export class InterfaceComponent implements OnInit{
  isAdmin: boolean = false;

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    const decodedToken: any = jwt_decode(token);
    
    if(decodedToken.role == "ADMIN") this.isAdmin = true
  }
}
