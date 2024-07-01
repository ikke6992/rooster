import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScheduledDayComponent } from './scheduled-day.component';

describe('ScheduledDayComponent', () => {
  let component: ScheduledDayComponent;
  let fixture: ComponentFixture<ScheduledDayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScheduledDayComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScheduledDayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
