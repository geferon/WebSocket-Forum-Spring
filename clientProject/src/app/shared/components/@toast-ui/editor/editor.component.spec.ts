import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToastUiEditorComponent } from './editor.component';

describe('ToastUiEditorComponent', () => {
  let component: ToastUiEditorComponent;
  let fixture: ComponentFixture<ToastUiEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ToastUiEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ToastUiEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
