import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

import { SettingsRoutingModule } from './settings-routing.module';
import { SettingsComponent } from './settings.component';
import { BasicUserLabelModule } from 'src/app/shared/components/basic-user-label/basic-user-label.component';
import { PageHeaderModule } from 'src/app/shared/components/page-header/page-header.component';
import { PasswordChangeDialogComponent } from './password-change/password-change.component';
import { MatDialogModule } from '@angular/material/dialog';


@NgModule({
	declarations: [
		SettingsComponent,
		PasswordChangeDialogComponent
	],
	imports: [
		CommonModule,
		SettingsRoutingModule,
		FormsModule,
		ReactiveFormsModule,
		FlexLayoutModule,
		MatFormFieldModule,
		MatInputModule,
		MatButtonModule,
		MatDialogModule,

		PageHeaderModule,
		BasicUserLabelModule
	]
})
export class SettingsModule { }
