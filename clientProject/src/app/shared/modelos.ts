import { DataSource } from "@angular/cdk/collections";
import { Observable, Subject } from "rxjs";
import { map, share, startWith, switchMap } from "rxjs/operators";

// Interfaces de modelos
export interface Usuario {
	id: number;
	username: string;
	email?: string;
	fechaRegistro: Date;
	avatar: string;
	roles?: string[];
}

export interface Icono {
	id: number;
	recurso: string;
}

export interface ReaccionIcono {
	id: number;
	recurso: string;
}

export interface Categoria {
	id: number;
	nombre: string;
	descripcion: string;
	color: string;
	icono: string;
	ultimoHilo?: HiloCategoria;
}

export interface Hilo {
	id: number;
	titulo: string;
	fechaCreacion: Date;
	icono?: Icono;
	creador: Usuario;
	publicacionesTotales: number;
	paginasTotales: number;
	categoria: Categoria | number;
}
export interface HiloCategoria extends Hilo {
	ultimaPublicacion?: Publicacion;
}
export interface HiloDTO extends Hilo {
	contenido: string;
}

export interface Reaccion {
	publicacion: Publicacion; // or id?
	usuario: Usuario; // or id?
	reaccionado: Date;
	icono: ReaccionIcono;
}

export interface Publicacion {
	id: number;
	idPublicacion: number;
	creador: Usuario;
	contenido: string;
	publicadoEn: Date;
	editadoEn: Date;
	reacciones?: Reaccion[];
	hilo?: Hilo;
	pagina: number;
}

export interface PublicacionCreate {
	id?: number;
	contenido: string;
	hilo: Hilo;
}
export interface PublicacionEdit {
	id: number;
	contenido: string;
}

export interface Subscripcion {
	hilo: Hilo;
	subscritoEn: Date;
	ultimaPublicacionLeida: Publicacion;
	publicacionesPendientes: number;
	ultimaPublicacionFecha: Date;
	ultimaPublicacionnextPublicacionFecha: Publicacion;
}


// Data sources
export interface PageRequest<T> {
	page: number;
	size: number;
}


export interface PageResult<T> {
	content: T[];
	totalElements: number;
	numberOfElements: number;
	size: number;
	number: number;
	totalPages: number;
	first: boolean;
	last: boolean;
	sort?: {
		property: string;
		direction: 'asc' | 'desc';
		ignoreCase: boolean;
		nullHandling: string;
	}[];
}



export type PaginationEndpoint<T> = (req: PageRequest<T>) => Observable<PageResult<T>>


export interface SimpleDataSource<T> extends DataSource<T> {
	connect(): Observable<T[]>;
	disconnect(): void;
}

export class PaginatedDataSource<T> implements SimpleDataSource<T> {
	private pageNumber = new Subject<number>();

	public pageNumber$ = this.pageNumber.asObservable();
	public page$: Observable<PageResult<T>>;

	constructor(
		endpoint: PaginationEndpoint<T>,
		page = 0,
		size = 20) {
		this.page$ = this.pageNumber.pipe(
			startWith(page),
			switchMap(page => endpoint({ page, size })),
			share()
		);
	}

	fetch(page: number): void {
		this.pageNumber.next(page);
	}

	connect(): Observable<T[]> {
		return this.page$.pipe(map(page => page.content));
	}

	disconnect(): void { }
}
