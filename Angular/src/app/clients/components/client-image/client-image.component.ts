import { Component, OnInit } from '@angular/core';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import { environment } from 'src/environments/environment.prod';

@Component({
  selector: 'app-client-image',
  templateUrl: './client-image.component.html',
  styleUrls: ['./client-image.component.css']
})

export class ClientImageComponent implements OnInit{
  imagePath: string = '';
  url:string = `${environment.apiURL}/image/`;
  constructor(private stockDataService: StockDataService){}

  ngOnInit() {
    this.stockDataService.image.subscribe(res=>{
      this.imagePath = res;
    })
  }
}
