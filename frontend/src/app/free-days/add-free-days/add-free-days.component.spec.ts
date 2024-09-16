import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFreeDaysComponent } from './add-free-days.component';

describe('AddFreeDaysComponent', () => {
  let component: AddFreeDaysComponent;
  let fixture: ComponentFixture<AddFreeDaysComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddFreeDaysComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddFreeDaysComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
