import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SetTeacherComponent } from './set-teacher.component';

describe('SetTeacherComponent', () => {
  let component: SetTeacherComponent;
  let fixture: ComponentFixture<SetTeacherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SetTeacherComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SetTeacherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
