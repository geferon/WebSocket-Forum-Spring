import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatRippleModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { TimeagoModule } from 'ngx-timeago';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { BasicUserLabelModule } from 'src/app/shared/components/basic-user-label/basic-user-label.component';


@NgModule({
	declarations: [HomeComponent],
	imports: [
		CommonModule,
		MatCardModule,
		MatRippleModule,
		MatIconModule,
		FlexLayoutModule,
		MatTooltipModule,
		MatButtonModule,
		MatMenuModule,
		TimeagoModule,

		BasicUserLabelModule,

		HomeRoutingModule
	]
})
export class HomeModule { }
