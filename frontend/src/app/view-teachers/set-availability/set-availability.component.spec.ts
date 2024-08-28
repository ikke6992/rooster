import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetAvailabilityComponent } from './set-availability.component';

describe('SetAvailabilityComponent', () => {
  let component: SetAvailabilityComponent;
  let fixture: ComponentFixture<SetAvailabilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SetAvailabilityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SetAvailabilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
