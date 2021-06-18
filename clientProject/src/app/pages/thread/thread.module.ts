import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { TimeagoModule } from 'ngx-timeago';

import { ThreadRoutingModule } from './thread-routing.module';
import { ThreadComponent } from './thread.component';
import { PageHeaderModule } from 'src/app/shared/components/page-header/page-header.component';
import { ToastUiModule } from 'src/app/shared/components/@toast-ui/toast-ui.module';
import { BasicUserLabelModule } from 'src/app/shared/components/basic-user-label/basic-user-label.component';
import { BasicPaginationModule } from 'src/app/shared/components/basic-pagination/basic-pagination.component';


@NgModule({
	declarations: [
		ThreadComponent
	],
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		FlexLayoutModule,
		MatTooltipModule,
		MatButtonModule,
		MatMenuModule,
		MatIconModule,
		TimeagoModule,

		PageHeaderModule,
		ToastUiModule,
		BasicUserLabelModule,
		BasicPaginationModule,

		ThreadRoutingModule
	]
})
export class ThreadModule { }
