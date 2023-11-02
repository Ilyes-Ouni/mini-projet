import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientImageComponent } from './client-image.component';

describe('ClientImageComponent', () => {
  let component: ClientImageComponent;
  let fixture: ComponentFixture<ClientImageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientImageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClientImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
