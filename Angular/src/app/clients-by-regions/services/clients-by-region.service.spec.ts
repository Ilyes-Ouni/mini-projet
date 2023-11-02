import { TestBed } from '@angular/core/testing';

import { ClientsByRegionService } from './clients-by-region.service';

describe('ClientsByRegionService', () => {
  let service: ClientsByRegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientsByRegionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
