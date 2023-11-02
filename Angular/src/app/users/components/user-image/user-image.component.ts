import { Component, OnInit } from '@angular/core';
import { StockDataService } from 'src/app/shared/services/stock-data.service';
import { environment } from 'src/environments/environment.prod';

@Component({
  selector: 'app-user-image',
  templateUrl: './user-image.component.html',
  styleUrls: ['./user-image.component.css']
})

export class UserImageComponent implements OnInit{
  imagePath: string = '';
  url:string = `${environment.apiURL}/image/`;
  constructor(private stockDataService: StockDataService){}

  ngOnInit() {
    this.stockDataService.image.subscribe(res=>{
      this.imagePath = res;
    })
  }
}
