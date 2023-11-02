import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StockDataService {
  private dataSource: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  id: Observable<number> = this.dataSource.asObservable();

  private imagePath: BehaviorSubject<string> = new BehaviorSubject<string>('');
  image: Observable<string> = this.imagePath.asObservable();

  constructor() { }

  setID(id: number) {
    this.dataSource.next(id);
  }

  setIamgePath(imagePath: string) {
    this.imagePath.next(imagePath);
  }
}
