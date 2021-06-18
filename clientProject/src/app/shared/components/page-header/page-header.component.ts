import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit, ChangeDetectionStrategy, NgModule, AfterViewInit, ViewChild, ElementRef, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Title } from '@angular/platform-browser';
import { LocationHistoryService } from 'src/app/shared/services/location-history.service';

@Component({
	selector: 'app-page-header',
	templateUrl: './page-header.component.html',
	styleUrls: ['./page-header.component.scss']
})
export class PageHeaderComponent implements AfterViewInit {
	@Input()
	public backButton = true;

	@Input()
	public defaultURL: string | any[];

	@ViewChild('content')
	public content: ElementRef;

	public canGoBack = this.history.canGoBack();

	constructor(
		private title: Title,
		private history: LocationHistoryService
	) { }

	ngAfterViewInit(): void {
		setTimeout(() => {
			let text = this.content.nativeElement.innerText?.trim() ?? "";
			if (text != "") {
				this.title.setTitle(text);
			}
		}, 0);
	}

	goBack(): void {
		this.history.goBack(this.defaultURL);
	}

}


@NgModule({
	imports: [
		CommonModule,
		HttpClientModule,
		MatIconModule,
		MatButtonModule
	],
	exports: [PageHeaderComponent],
	declarations: [PageHeaderComponent]
})
export class PageHeaderModule { }
