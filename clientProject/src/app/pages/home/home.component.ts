import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/shared/services/auth.service';
import { DataService } from 'src/app/shared/services/data.service';
import { Categoria } from 'src/app/shared/modelos';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	constructor(
		public auth: AuthService,
		public data: DataService
	) { }

	public categorias$: Observable<Categoria[]>;

	ngOnInit() {
		this.loadData();
	}

	public loadData() {
		this.categorias$ = this.data.getCategoriasLive();
	}

	public deleteCategoria(categoria: Categoria) {
		this.data.deleteCategoria(categoria)
			.subscribe();
	}

}
