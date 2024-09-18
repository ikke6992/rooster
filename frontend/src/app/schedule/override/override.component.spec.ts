import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OverrideComponent } from './override.component';

describe('OverrideComponent', () => {
  let component: OverrideComponent;
  let fixture: ComponentFixture<OverrideComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OverrideComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OverrideComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
