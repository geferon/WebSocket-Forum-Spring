import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectionStrategy, NgModule, Input, OnChanges, SimpleChange, Output, EventEmitter, HostBinding } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';

@Component({
	selector: 'app-basic-pagination',
	templateUrl: './basic-pagination.component.html',
	styleUrls: ['./basic-pagination.component.scss']
})
export class BasicPaginationComponent implements OnInit, OnChanges {

	constructor() { }

	@Input() totalPages: number;
	@Input()
	get page(): number { return this._page; }
	set page(value: number) {
		this._page = value;
		this.recalculatePages();
	}
	private _page: number;
	@Output() pageChange: EventEmitter<number> = new EventEmitter();
	@Input() maxSize: number = 4;

	@HostBinding('class.pagination-small')
	private _small: boolean = false;

	@Input()
	get small() {return this._small;}
	set small(value: boolean) {this._small = value;}

	@Input() routeBase: string | any[] = null;


	public pages: number[] = [];

	public getPageLink(page: number): string | any[] {
		return Array.isArray(this.routeBase) ?
			[...this.routeBase, page] :
			this.routeBase + (this.routeBase.endsWith('/') ? '' : '/') + page;
	}

	public openPaginator($event: any): void {
		// TODO
	}

	goToPage(page: number): void {
		this.page = page;
		this.pageChange.emit(page);
		this.recalculatePages();
	}

	ngOnInit() {
		this.recalculatePages();
	}

	ngOnChanges(changes: { [propName: string]: SimpleChange }) {
		// En caso de que haya un cambio en cualquiera de estos dos campos recalculamos las paginas
		let fieldsToCheck = ['totalPages', 'maxSize'];
		for (let field of fieldsToCheck) {
			if (changes[field] && changes[field].currentValue != changes[field].previousValue) {
				this.recalculatePages();
				break;
			}
		}
	}

	private recalculatePages() {
		this.pages = [];
		if (this.totalPages < 2) {
			return;
		}

		if (this.totalPages > this.maxSize) {
			if (this.page) {
				this.pages = this.getUnfocusedRange(this.totalPages, this.maxSize);
			} else {
				this.pages = this.getFocusedRange(this.totalPages, this.maxSize, this.page);
			}
		} else {
			this.pages = this.getFullRange(this.totalPages);
		}
	}

	private getUnfocusedRange(totalPages: number, maxSize: number) {
		const output = [];
		const center = Math.ceil(maxSize / 2);

		// Contar desde la primera pagina hasta el centro
		for (let i = 1; i <= center; i++) {
			output.push(i);
		}

		// Separador
		output.push(null);

		// Contar desde la ultima pagina hasta el centro
		for (let i = center; i > 0; i--) {
			output.push(totalPages - (i - 1));
		}

		return output;
	}

	private getFocusedRange(totalPages: number, maxSize: number, currentPage: number) {
		const output = [];
		const center = Math.ceil(maxSize / 2);

		// Si la pagina actual esta cerca del inicio
		if (currentPage <= center) {
			for (let i = 1; i <= maxSize; i++) {
				output.push(i);
			}
			output[maxSize - 2] = null;
			output[maxSize - 1] = totalPages;
		}

		// Si la pagina actual se encuentra cerca del centro
		if (currentPage > center && currentPage <= totalPages - center) {
			for (let i = center - 1; i > 0; i--) {
				output.push(currentPage - i);
			}
			for (let i = 0; i < center; i++) {
				output.push(currentPage + i);
			}
			output[0] = 1;
			output[1] = null;
			output[maxSize - 2] = null;
			output[maxSize - 1] = totalPages;
		}

		// Si el usuario esta en una pagina cerca del final
		if (currentPage > totalPages - center) {
			for (let i = maxSize; i > 0; i--) {
				output.push(totalPages - (i - 1));
			}
			output[0] = 1;
			output[1] = null;
		}

		return output;
	}

	private getFullRange(totalPages: number) {
		// Crear una array desde 1 hasta totalPages
		return Array.from({length: totalPages}, (v, k) => k + 1);
	}

}

@NgModule({
	imports: [
		CommonModule,
		RouterModule,
		FlexLayoutModule,
		MatButtonModule
	],
	exports: [BasicPaginationComponent],
	declarations: [BasicPaginationComponent]
})
export class BasicPaginationModule { }
