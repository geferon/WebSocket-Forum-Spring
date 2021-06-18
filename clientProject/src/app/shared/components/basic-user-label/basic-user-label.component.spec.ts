import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicUserLabelComponent } from './basic-user-label.component';

describe('BasicUserLabelComponent', () => {
  let component: BasicUserLabelComponent;
  let fixture: ComponentFixture<BasicUserLabelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BasicUserLabelComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BasicUserLabelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
