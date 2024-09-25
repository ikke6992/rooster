import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVacationComponent } from './add-vacation.component';

describe('AddVacationComponent', () => {
  let component: AddVacationComponent;
  let fixture: ComponentFixture<AddVacationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddVacationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddVacationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
