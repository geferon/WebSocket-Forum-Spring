import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TimeagoModule } from 'ngx-timeago';
import { MatTooltipModule } from '@angular/material/tooltip';

import { ProfileRoutingModule } from './profile-routing.module';
import { ProfileComponent } from './profile.component';
import { BasicUserLabelModule } from 'src/app/shared/components/basic-user-label/basic-user-label.component';
import { PageHeaderModule } from 'src/app/shared/components/page-header/page-header.component';
import { ToastUiModule } from 'src/app/shared/components/@toast-ui/toast-ui.module';
import { BasicPaginationModule } from 'src/app/shared/components/basic-pagination/basic-pagination.component';


@NgModule({
	declarations: [
		ProfileComponent
	],
	imports: [
		CommonModule,
		ProfileRoutingModule,
		FormsModule,
		ReactiveFormsModule,
		FlexLayoutModule,
		TimeagoModule,
		MatTooltipModule,

		ToastUiModule,
		PageHeaderModule,
		BasicUserLabelModule,
		BasicPaginationModule
	]
})
export class ProfileModule { }
