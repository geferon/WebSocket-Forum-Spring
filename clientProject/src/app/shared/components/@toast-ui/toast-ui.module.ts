import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastUiEditorComponent } from './editor/editor.component';
import { ToastUiViewerComponent } from './viewer/viewer.component';


@NgModule({
	imports: [
		CommonModule
	],
	declarations: [
		ToastUiEditorComponent,
		ToastUiViewerComponent
	],
	exports: [
		ToastUiEditorComponent,
		ToastUiViewerComponent
	]
})
export class ToastUiModule { }
