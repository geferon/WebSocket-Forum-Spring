import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatCardModule } from '@angular/material/card';
import { MatRippleModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatButtonModule } from '@angular/material/button';
import { TimeagoModule } from 'ngx-timeago';

import { CategoryRoutingModule } from './category-routing.module';
import { CategoryComponent } from './category.component';
import { PageHeaderModule } from 'src/app/shared/components/page-header/page-header.component';
import { BasicUserLabelModule } from 'src/app/shared/components/basic-user-label/basic-user-label.component';
import { BasicPaginationModule } from 'src/app/shared/components/basic-pagination/basic-pagination.component';


@NgModule({
	declarations: [CategoryComponent],
	imports: [
		CommonModule,
		FlexLayoutModule,
		MatCardModule,
		MatButtonModule,
		MatRippleModule,
		MatIconModule,
		MatPaginatorModule,
		MatTooltipModule,
		TimeagoModule,

		PageHeaderModule,
		BasicUserLabelModule,
		BasicPaginationModule,

		CategoryRoutingModule
	]
})
export class CategoryModule { }
