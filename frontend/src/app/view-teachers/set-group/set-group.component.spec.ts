import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetGroupComponent } from './set-group.component';

describe('SetGroupComponent', () => {
  let component: SetGroupComponent;
  let fixture: ComponentFixture<SetGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SetGroupComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SetGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
