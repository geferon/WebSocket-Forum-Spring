import { DataSource } from '@angular/cdk/collections';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InjectableRxStompConfig, RxStompService } from '@stomp/ng2-stompjs';
import { concat, merge, Observable, of, Subject } from 'rxjs';
import { filter, map, share, startWith, switchMap, tap } from 'rxjs/operators';

import { AuthService } from './auth.service';
import { convertToDate } from './date.interceptor';
import { Categoria, Hilo, HiloCategoria, HiloDTO, PageResult, Publicacion, PublicacionCreate, PublicacionEdit, Subscripcion, Usuario } from '../modelos';

export const BASE_HREF = '/api';

@Injectable({
	providedIn: 'root'
})
export class DataService {
	constructor(
		private http: HttpClient,
		private auth: AuthService,
		private rxStompService: RxStompService,
		private rxConfig: InjectableRxStompConfig
	) {
		this.initStomp();
	}

	private baseHref = '/api';

	private parse<T>(body: string): T {
		return convertToDate(JSON.parse(body));
	}

	public getCategorias() {
		return this.http.get<Categoria[]>(`${BASE_HREF}/categoria`);
	}

	public getCategoriasLive() {
		let categoriasCache: Categoria[] = [];
		return concat(
			this.getCategorias()
				.pipe(
					tap((categorias: Categoria[]) => categoriasCache = categorias)
				),
			merge(
				this.rxStompService.watch('/topic/categorias/updated')
					.pipe(
						map(m => {
							let categoria = this.parse<Categoria>(m.body);
							categoriasCache[categoriasCache.findIndex(c => c.id == categoria.id)] = categoria;
							return categoriasCache;
						})
					),
				this.rxStompService.watch('/topic/categorias/inserted')
					.pipe(
						map(m => {
							let categoria = this.parse<Categoria>(m.body);
							categoriasCache.push(categoria);
							return categoriasCache;
						})
					),
				this.rxStompService.watch('/topic/categorias/deleted')
					.pipe(
						map(m => {
							let categoria = this.parse<Categoria>(m.body);
							categoriasCache.splice(categoriasCache.findIndex(c => c.id == categoria.id), 1);
							return categoriasCache;
						})
					)
			)
		);
	}

	public getCategoria(categoria: number) {
		return this.http.get<Categoria>(`${BASE_HREF}/categoria/${categoria}`);
	}

	public getCategoriaLive(categoria: number) {
		return concat(
			this.getCategoria(categoria),
			this.rxStompService.watch('/topic/categorias/updated')
				.pipe(
					map(m => this.parse<Categoria>(m.body)),
					filter(c => c.id == categoria)
				)
		);
	}

	public createCategoria(categoria: Categoria) {
		return this.http.post<Categoria>(`${BASE_HREF}/categoria`, categoria);
	}

	public deleteCategoria(categoria: Categoria | number) {
		return this.http.delete<Categoria>(`${BASE_HREF}/categoria/${typeof categoria === 'number' ? categoria : categoria.id}`);
	}

	public updateCategoria(categoria: Categoria) {
		return this.http.put<Categoria>(`${BASE_HREF}/categoria/${categoria.id}`, categoria.id);
	}

	public getHilos(categoria: number, page: number = 0, size: number = 40) {
		return this.http.get<PageResult<HiloCategoria>>(`${BASE_HREF}/hilo`, {
			params: {
				categoria,
				page,
				size
			}
		});
	}

	public getHilosLive(categoria: number, page: number = 0, size: number = 40) {
		let hilosCache: PageResult<HiloCategoria>;
		let sort = () => {
			hilosCache.content = hilosCache.content.sort((a, b) =>
				(b.ultimaPublicacion?.publicadoEn.getTime() ?? b.fechaCreacion.getTime()) -
				(a.ultimaPublicacion?.publicadoEn.getTime() ?? a.fechaCreacion.getTime())
			);
		};
		return concat(
			this.getHilos(categoria, page, size)
				.pipe(
					tap(hilos => hilosCache = hilos)),
					merge(
						this.rxStompService.watch(`/topic/categorias/${categoria}/hilos/inserted`)
							.pipe(
								map(m => this.parse<HiloCategoria>(m.body)),
								tap((h) => {
									hilosCache.totalElements++;
									hilosCache.numberOfElements++;
									hilosCache.totalPages = Math.ceil(hilosCache.totalElements / size);
								}),
								filter(h => hilosCache.number == 0 || hilosCache.content.some(hc => h.ultimaPublicacion.publicadoEn >= hc.ultimaPublicacion.publicadoEn)),
								switchMap(hilo => {
									let hiloId;
									if ((hiloId = hilosCache.content.findIndex(p => p.id == hilo.id)) != -1) {
										hilosCache.content[hiloId] = hilo;
									} else {
										if (hilosCache.number != 0) {
											return this.getHilos(categoria, page, size);
										}
										hilosCache.content.push(hilo);
									}
									sort();

									if (hiloId == -1) {
										hilosCache.content.pop();
									}

									return of(hilosCache);
								})
							),
						this.rxStompService.watch(`/topic/categorias/${categoria}/hilos/updated`)
							.pipe(
								map(m => this.parse<HiloCategoria>(m.body)),
								filter(h => hilosCache.number == 0 || hilosCache.content.some(hc => h.ultimaPublicacion.publicadoEn >= hc.ultimaPublicacion.publicadoEn)),
								switchMap(hilo => {
									let hiloId;
									if ((hiloId = hilosCache.content.findIndex(p => p.id == hilo.id)) != -1) {
										hilosCache.content[hiloId] = hilo;
									} else {
										if (hilosCache.number != 0) {
											return this.getHilos(categoria, page, size);
										}
										hilosCache.content.push(hilo);
									}
									sort();

									if (hiloId == -1) {
										hilosCache.content.pop();
									}

									return of(hilosCache);
								})
							),
						this.rxStompService.watch(`/topic/categorias/${categoria}/hilos/deleted`)
							.pipe(
								map(m => this.parse<HiloCategoria>(m.body)),
								filter(h => hilosCache.content.some(hc => hc.ultimaPublicacion.publicadoEn <= h.ultimaPublicacion.publicadoEn)),
								switchMap(h => this.getHilos(categoria, page, size)) // Volver a obtener todos si uno de las publicaciones de esta pagina fue borrada
							)
					)
			);
	}

	public getHilo(hilo: number) {
		return this.http.get<Hilo>(`${BASE_HREF}/hilo/${hilo}`);
	}

	public getHiloLive(hilo: number) {
		return concat(
			this.getHilo(hilo),
			this.rxStompService.watch(`/topic/hilos/updated`)
				.pipe(
					map(m => this.parse<Hilo>(m.body)),
					filter(h => h.id == hilo)
				)
		);
	}

	public createHilo(hilo: HiloDTO) {
		return this.http.post<Hilo>(`${BASE_HREF}/hilo`, hilo);
	}

	public editHilo(hilo: Partial<Hilo>) {
		return this.http.put<Hilo>(`${BASE_HREF}/hilo/${hilo.id}`, hilo);
	}

	public deleteHilo(hilo: Hilo | number) {
		return this.http.delete<Hilo>(`${BASE_HREF}/hilo/${typeof hilo === 'number' ? hilo : hilo.id}`);
	}

	public getPublicaciones(hilo: number, page: number = 0, size: number = 10) {
		return this.http.get<PageResult<Publicacion>>(`${BASE_HREF}/publicacion`, {
			params: {
				hilo,
				page,
				size
			}});
	}

	public getPublicacionesLive(hilo: number, page: number = 0, size: number = 10) {
		let publicacionesCache: PageResult<Publicacion>;
		return concat(
			this.getPublicaciones(hilo, page, size)
				.pipe(tap(publicaciones => publicacionesCache = publicaciones)),
				merge(
					this.rxStompService.watch(`/topic/hilos/${hilo}/publicaciones/inserted`)
						.pipe(
							filter(() => publicacionesCache.totalPages == (page + 1) && publicacionesCache.totalElements < size),
							map(m => {
								publicacionesCache.content.push(this.parse(m.body));
								publicacionesCache.totalElements++;
								publicacionesCache.numberOfElements++;
								return publicacionesCache;
							})
						),
					this.rxStompService.watch(`/topic/hilos/${hilo}/publicaciones/updated`)
						.pipe(
							map(m => {
								let publicacion = this.parse<Publicacion>(m.body);
								let publicacionId;
								if ((publicacionId = publicacionesCache.content.findIndex(p => p.id == publicacion.id)) != -1) {
									publicacionesCache.content[publicacionId] = publicacion;
								}
								return publicacionesCache;
							})
						)
				)
		);
	}

	public createPublicacion(publicacion: PublicacionCreate) {
		return this.http.post<Publicacion>(`${BASE_HREF}/publicacion`, publicacion);
	}

	public editPublicacion(publicacion: PublicacionEdit) {
		return this.http.put<Publicacion>(`${BASE_HREF}/publicacion/${publicacion.id}`, publicacion);
	}

	public deletePublicacion(publicacion: Publicacion) {
		return this.http.delete<Publicacion>(`${BASE_HREF}/publicacion/${publicacion.id}`);
	}

	public getPublicacionesUsuario(usuario: number, page: number = 0, size: number = 10) {
		return this.http.get<PageResult<Publicacion>>(`${BASE_HREF}/publicacion`, {
			params: {
				usuario,
				page,
				size
			}
		});
	}

	public getPublicacionesUsuarioLive(usuario: number, page: number = 0, size: number = 10) {
		let publicacionesCache: PageResult<Publicacion>;
		return concat(
			this.getPublicacionesUsuario(usuario, page, size)
				.pipe(tap(publicaciones => publicacionesCache = publicaciones)),
			merge(
				this.rxStompService.watch(`/topic/usuarios/${usuario}/publicaciones/inserted`)
					.pipe(
						filter(() => publicacionesCache.totalPages == (page + 1) && publicacionesCache.totalElements < size),
						map(m => {
							publicacionesCache.content.push(this.parse(m.body));
							publicacionesCache.totalElements++;
							publicacionesCache.numberOfElements++;
							return publicacionesCache;
						})
					),
				this.rxStompService.watch(`/topic/usuarios/${usuario}/publicaciones/updated`)
					.pipe(
						map(m => {
							let publicacion = this.parse<Publicacion>(m.body);
							let publicacionId;
							if ((publicacionId = publicacionesCache.content.findIndex(p => p.id == publicacion.id)) != -1) {
								publicacionesCache.content[publicacionId] = publicacion;
							}
							return publicacionesCache;
						})
					)
			)
		);
	}

	public getUsuario(id: number) {
		return this.http.get<Usuario>(`${BASE_HREF}/usuario/${id}`);
	}

	public updateUsuario(id: number, usuario: Usuario) {
		let data: Usuario | FormData = usuario;
		if (usuario.avatar && typeof usuario.avatar != 'string') {
			// headers['Content-Type'] = 'multipart/form-data';

			data = new FormData();
			for (let [key, value] of Object.entries(usuario)) {
				if (value instanceof File) {
					data.append(key, value, value.name);
				} else {
					data.append(key, value);
				}
			}
		}
		return this.http.post<Usuario>(`${BASE_HREF}/usuario/${id}`, data);
	}


	private initStomp(): void {
		this.auth.token$.subscribe(t => {
			let stompConfig: InjectableRxStompConfig = this.rxConfig;
			if (this.auth.isLoggedIn()) {
				stompConfig = Object.assign({}, stompConfig, {
					connectHeaders: {
						'X-Authorization': this.auth.token
					}
				});
			}

			this.rxStompService.configure(stompConfig);
		});
		this.rxStompService.activate();
	}





	// Subscripciones

	public getSubscriptions() {
		return this.http.get<Subscripcion[]>(`${BASE_HREF}/subscripcion`);
	}

	// Lo guardo en una variable local y no lo genero en el metodo para hacerlo un singleton
	private subscripcionesCache: Subscripcion[];
	private subscripcionesSubject$ = new Subject<Subscripcion[]>();
	private subscriptionsLive$: Observable<Subscripcion[]> = this.auth.user$
		.pipe(
			switchMap(u => {
				if (u) return concat(
					this.getSubscriptions()
						.pipe(
							tap(subscripciones => this.subscripcionesCache = subscripciones)
						),
					merge(
						this.rxStompService.watch(`/user/topic/subscripciones`)
							.pipe(
								map(m => {
									let subscripcion = this.parse<Subscripcion>(m.body);

									this.receivedSubscripcion(subscripcion);

									return this.subscripcionesCache;
								})
							),
						this.rxStompService.watch(`/user/topic/subscripciones/deleted`)
							.pipe(
								map(m => {
									let subscripcion = this.parse<Subscripcion>(m.body);

									let idx;
									if ((idx = this.subscripcionesCache.findIndex(s => s.hilo.id == subscripcion.hilo.id)) != -1) {
										this.subscripcionesCache.splice(idx, 1);
									}

									return this.subscripcionesCache;
								})
							),
						this.subscripcionesSubject$.asObservable()
					)
				);
				this.subscripcionesCache = [];
				return of([] as Subscripcion[]);
			}),
			map(subs => subs.sort((a, b) => a.ultimaPublicacionFecha.getTime() - b.ultimaPublicacionFecha.getTime())),
			share()
		);

	private receivedSubscripcion(subscripcion: Subscripcion): void {
		let idx;
		if ((idx = this.subscripcionesCache.findIndex(s => s.hilo.id == subscripcion.hilo.id)) != -1) {
			this.subscripcionesCache[idx] = subscripcion;
		} else {
			this.subscripcionesCache.push(subscripcion);
		}
	}

	public getSubscriptionsLive(): Observable<Subscripcion[]> {
		return this.subscriptionsLive$;
	}

	public isSubscribed(hilo: Hilo | number): boolean {
		return this.subscripcionesCache.some(s => s.hilo.id == (typeof hilo === 'number' ? hilo : hilo.id));
	}

	public isPostRead(publicacion: Publicacion, hilo: Hilo | number): boolean {
		return this.subscripcionesCache.find(s => s.hilo.id == (typeof hilo === 'number' ? hilo : hilo.id))?.ultimaPublicacionLeida?.id >= publicacion.id ?? true;
	}

	public subscribeToHilo(hilo: Hilo): Observable<Subscripcion> {
		return this.http.post<Subscripcion>(`${BASE_HREF}/subscripcion`, {
			idHilo: hilo.id
		})
			.pipe(
				tap(s => {
					this.receivedSubscripcion(s);
					this.subscripcionesSubject$.next(this.subscripcionesCache);
				})
			);
	}

	public unsubscribeFromHilo(hilo: Hilo): Observable<Subscripcion> {
		return this.http.delete<Subscripcion>(`${BASE_HREF}/subscripcion`, {
			params: {
				hilo: hilo.id
			}
		})
			.pipe(
				tap(subscripcion => {
					let idx;
					if ((idx = this.subscripcionesCache.findIndex(s => s.hilo.id == subscripcion.hilo.id)) != -1) {
						this.subscripcionesCache.splice(idx, 1);
					}
					this.subscripcionesSubject$.next(this.subscripcionesCache);
				})
			);
	}

	public markAsRead(publicacion: Publicacion): Observable<Subscripcion> {
		return this.http.post<Subscripcion>(`${BASE_HREF}/subscripcion/read`, {
			idPublicacion: publicacion.id,
			lastRead: new Date()
		})
			.pipe(
				tap(s => {
					this.receivedSubscripcion(s);
					this.subscripcionesSubject$.next(this.subscripcionesCache);
				})
			);
	}
}



