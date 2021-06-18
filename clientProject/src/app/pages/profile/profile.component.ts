import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { DataService } from 'src/app/shared/services/data.service';
import { Categoria, PaginatedDataSource, Publicacion, Usuario } from 'src/app/shared/modelos';

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

	public usuarioID: number;
	public usuario$: Observable<Usuario>;

	public publicaciones$: Observable<Publicacion[]>;
	public publicacionesDataSource: PaginatedDataSource<Publicacion>;

	// private _subscriptions = new Subscription();

	constructor(
		private route: ActivatedRoute,
		private data: DataService
	) { }

	ngOnInit(): void {
		const routeParams = this.route.snapshot.paramMap;
		this.usuarioID = parseInt(routeParams.get('id'));
		this.usuario$ = this.data.getUsuario(this.usuarioID);

		this.publicacionesDataSource = new PaginatedDataSource<Publicacion>(
			request => this.data.getPublicacionesUsuarioLive(this.usuarioID, request.page, request.size)
		);

		this.publicaciones$ = this.publicacionesDataSource.connect();
	}

	ngOnDestroy(): void {
		this.publicacionesDataSource.disconnect();
	}

	getCategoria(publicacion: Publicacion): Categoria {
		return publicacion.hilo.categoria as Categoria;
	}

}
