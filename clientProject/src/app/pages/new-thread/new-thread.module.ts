import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

import { NewThreadRoutingModule } from './new-thread-routing.module';
import { NewThreadComponent } from './new-thread.component';
import { PageHeaderModule } from 'src/app/shared/components/page-header/page-header.component';
import { ToastUiModule } from 'src/app/shared/components/@toast-ui/toast-ui.module';


@NgModule({
	declarations: [
		NewThreadComponent
	],
	imports: [
		CommonModule,
		NewThreadRoutingModule,
		FlexLayoutModule,
		FormsModule,
		ReactiveFormsModule,
		MatFormFieldModule,
		MatInputModule,
		MatButtonModule,
		MatIconModule,

		PageHeaderModule,
		ToastUiModule
	]
})
export class NewThreadModule { }
