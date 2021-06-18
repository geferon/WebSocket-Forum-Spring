import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { filter, tap } from 'rxjs/operators';
import { AuthService } from 'src/app/shared/services/auth.service';
import { DataService } from 'src/app/shared/services/data.service';
import { Hilo, PaginatedDataSource, Publicacion, PublicacionCreate } from 'src/app/shared/modelos';

@Component({
	selector: 'app-thread',
	templateUrl: './thread.component.html',
	styleUrls: ['./thread.component.scss']
})
export class ThreadComponent implements OnInit, OnDestroy {

	public hiloID: number = null;
	public page: number = null;
	public hilo$: Observable<Hilo>;
	public publicaciones$: Observable<Publicacion[]>;
	public publicacionesDataSource: PaginatedDataSource<Publicacion>;
	public contentMinLength = 10;
	public contentMaxLength = 10000;

	public nuevaPublicacion = new FormGroup({
		contenido: new FormControl('', [Validators.required, Validators.minLength(this.contentMinLength), Validators.maxLength(this.contentMaxLength)])
	});

	public threadEdited: Publicacion = null;

	private _subscriptions = new Subscription();

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		public data: DataService,
		public auth: AuthService
	) { }

	ngOnInit(): void {
		const routeParams = this.route.snapshot.paramMap;
		this.hiloID = parseInt(routeParams.get('id'));
		this.page = parseInt(routeParams.get('page')) - 1; // Empieza en 1 pero en realidad es la pagina 0
		this.hilo$ = this.data.getHiloLive(this.hiloID);

		this.publicacionesDataSource = new PaginatedDataSource<Publicacion>(
			request => this.data.getPublicacionesLive(this.hiloID, request.page, request.size)
				.pipe(
					tap(p => {
						let lastPost = p.content[p.content.length - 1];
						if (p.content.length > 0 && this.data.isSubscribed(this.hiloID) && !this.data.isPostRead(lastPost, this.hiloID) &&
							(!this.auth.user || lastPost.creador.id != this.auth.user?.id))
							this.data.markAsRead(lastPost).subscribe();
					})
				),
			this.page
		);

		this.publicaciones$ = this.publicacionesDataSource.connect();
		this._subscriptions.add(
			this.route.params.subscribe(p => {
				let page = parseInt(p['page']) - 1;
				if (page != this.page) {
					this.publicacionesDataSource.fetch(page);
				}
			})
		);
		this._subscriptions.add(
			this.publicacionesDataSource.pageNumber$.subscribe(page => {
				this.page = page;

				this.router.navigate(['../' + (page + 1)], {relativeTo: this.route});
			})
		);
	}

	ngOnDestroy(): void {
		this.publicacionesDataSource.disconnect();
		this._subscriptions.unsubscribe();
	}

	onSubmit(): void {
		let publicacion: PublicacionCreate = this.nuevaPublicacion.value;
		publicacion.hilo = {id: this.hiloID} as Hilo;

		this.data.createPublicacion(publicacion)
		.subscribe(nuevaPublicacion => {
			this.router.navigate(
				nuevaPublicacion.pagina != (this.page + 1) ? ['../' + nuevaPublicacion.pagina] : [],
				{ relativeTo: this.route, fragment: 'post-' + nuevaPublicacion.id }
			);
			this.nuevaPublicacion.setValue({
				contenido: ''
			});
		});
	}

	changePage(page: number) {
		this.publicacionesDataSource.fetch(page - 1);
	}

	setEditing(publicacion: Publicacion): void {
		this.threadEdited = Object.assign({}, publicacion);
		// this.threadEdited = JSON.parse(JSON.stringify(publicacion));
	}

	canEditThread(hilo: Hilo): boolean {
		return hilo.creador.id == this.auth.user?.id || this.auth.isRole('ROLE_ADMIN');
	}

	canEdit(publicacion: Publicacion): boolean {
		return this.auth.isRole('ROLE_ADMIN') || publicacion?.creador.id == this.auth.user?.id;
	}

	edit(publicacion: Publicacion): void {
		this.data.editPublicacion(publicacion)
			.subscribe(() => {
				this.threadEdited = null;
			});
	}

	deletePublicacion(publicacion: Publicacion): void {
		this.data.deletePublicacion(publicacion).subscribe();
	}

	canDelete(publicacion: Publicacion): boolean {
		return this.auth.isRole('ROLE_ADMIN');
	}

	deleteHilo(hilo: Hilo) {
		this.data.deleteHilo(hilo)
			.subscribe(() => {
				this.router.navigate(['categorias', (typeof hilo.categoria == 'number' ? hilo.categoria : hilo.categoria.id)]);
			});
	}

}
