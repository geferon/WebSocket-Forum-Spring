import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastUiViewerComponent } from './viewer.component';

describe('ViewerComponent', () => {
  let component: ToastUiViewerComponent;
  let fixture: ComponentFixture<ToastUiViewerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ToastUiViewerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ToastUiViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
