import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatTabsModule } from '@angular/material/tabs';

import { LoginDialogComponent } from './login-dialog/login-dialog.component';



@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		FlexLayoutModule,
		MatButtonModule,
		MatIconModule,
		MatInputModule,
		MatDialogModule,
		MatFormFieldModule,
		MatTabsModule
	],
	declarations: [
		LoginDialogComponent
	],
	entryComponents: [
		LoginDialogComponent
	]
})
export class DialogsModule { }
