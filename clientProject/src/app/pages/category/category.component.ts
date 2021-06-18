import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/shared/services/auth.service';

import { DataService } from 'src/app/shared/services/data.service';
import { Categoria, Hilo, HiloCategoria, PaginatedDataSource } from 'src/app/shared/modelos';

@Component({
	templateUrl: './category.component.html',
	styleUrls: ['./category.component.scss']
})
export class CategoryComponent implements OnInit, OnDestroy {

	public categoriaID: number = null;
	public categoria$: Observable<Categoria>;
	public hilos$: Observable<HiloCategoria[]>;
	public hilosDataSource: PaginatedDataSource<HiloCategoria>;

	constructor(
		private route: ActivatedRoute,
		private data: DataService,
		public auth: AuthService
	) { }

	ngOnInit(): void {
		const routeParams = this.route.snapshot.paramMap;
		this.categoriaID = parseInt(routeParams.get('id'));
		this.categoria$ = this.data.getCategoria(this.categoriaID);
		this.hilosDataSource = new PaginatedDataSource<HiloCategoria>(
			request => this.data.getHilosLive(this.categoriaID, request.page, request.size),
			0, 40
		);

		this.hilos$ = this.hilosDataSource.connect();
	}

	ngOnDestroy(): void {
		this.hilosDataSource.disconnect();
	}

}
