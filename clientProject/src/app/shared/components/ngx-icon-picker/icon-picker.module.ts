import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

import { IconPickerService } from './icon-picker.service';
import { IconPickerComponent } from './icon-picker.component';
import { IconPickerDirective } from './icon-picker.directive';
import { TextDirective } from './text.directive';
import { SearchIconPipe } from './search-icon.pipe';

@NgModule({
	imports: [
		CommonModule,
		MatButtonModule,
		MatMenuModule,
		FlexLayoutModule,
		MatInputModule,
		MatFormFieldModule
	],
	providers: [
		IconPickerService
	],
	declarations: [
		IconPickerComponent,
		IconPickerDirective,
		TextDirective,
		SearchIconPipe
	],
	exports: [
		IconPickerDirective
	],
	entryComponents: [
		IconPickerComponent
	]
})
export class IconPickerModule {
}
