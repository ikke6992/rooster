import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewGroupsComponent } from './view-groups.component';

describe('ViewGroupsComponent', () => {
  let component: ViewGroupsComponent;
  let fixture: ComponentFixture<ViewGroupsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ViewGroupsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ViewGroupsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
