import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewFieldsComponent } from './view-fields.component';

describe('ViewFieldsComponent', () => {
  let component: ViewFieldsComponent;
  let fixture: ComponentFixture<ViewFieldsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewFieldsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewFieldsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
