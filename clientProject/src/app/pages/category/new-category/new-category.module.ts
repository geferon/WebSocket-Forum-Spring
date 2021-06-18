import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ColorPickerModule } from 'ngx-color-picker';

import { NewCategoryRoutingModule } from './new-category-routing.module';
import { NewCategoryComponent } from './new-category.component';
import { IconPickerModule } from 'src/app/shared/components/ngx-icon-picker';
import { PageHeaderModule } from 'src/app/shared/components/page-header/page-header.component';


@NgModule({
	declarations: [
		NewCategoryComponent
	],
	imports: [
		CommonModule,
		NewCategoryRoutingModule,
		FlexLayoutModule,
		FormsModule,
		ReactiveFormsModule,
		MatFormFieldModule,
		MatInputModule,
		MatButtonModule,
		MatIconModule,
		ColorPickerModule,

		IconPickerModule,
		PageHeaderModule
	]
})
export class NewThreadModule { }
