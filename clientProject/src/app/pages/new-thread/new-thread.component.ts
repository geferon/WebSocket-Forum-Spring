import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { DataService } from 'src/app/shared/services/data.service';

@Component({
	selector: 'app-new-thread',
	templateUrl: './new-thread.component.html',
	styleUrls: ['./new-thread.component.scss']
})
export class NewThreadComponent implements OnInit {

	public categoriaID: number = null;
	public hiloID: number = null;
	private lastPublicacionID: number = null;
	public contentMinLength = 10;
	public contentMaxLength = 10000;

	public nuevoHilo = new FormGroup({
		categoria: new FormControl({id: 0}),
		titulo: new FormControl('', [Validators.required]),
		contenido: new FormControl('', [Validators.required, Validators.minLength(this.contentMinLength), Validators.maxLength(this.contentMaxLength)])
	});

	public isNew = false;

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private data: DataService
	) { }

	ngOnInit(): void {
		const routeParams = this.route.snapshot.paramMap;
		this.categoriaID = parseInt(routeParams.get('idCategoria'));
		this.hiloID = parseInt(routeParams.get('idHilo'));

		if (this.categoriaID) {
			this.nuevoHilo.controls.categoria.setValue({id: this.categoriaID});
			this.isNew = true;
		} else {
			forkJoin([
				this.data.getHilo(this.hiloID),
				this.data.getPublicaciones(this.hiloID, 0, 1)
			])
			.subscribe(([hilo, publicaciones]) => {
				let publicacion = publicaciones.content[0];

				this.lastPublicacionID = publicacion.id;
				this.nuevoHilo.patchValue({
					titulo: hilo.titulo,
					contenido: publicacion.contenido
				});
			});
		}
	}

	publicar(): void {
		if (this.isNew) {
			this.data.createHilo(this.nuevoHilo.value)
			.subscribe(res => {
				this.router.navigate(['/hilos', res.id, 1]);
			});
		} else {
			forkJoin([
				this.data.editHilo({
					id: this.hiloID,
					titulo: this.nuevoHilo.controls.titulo.value
				}),
				this.data.editPublicacion({
					id: this.lastPublicacionID,
					contenido: this.nuevoHilo.controls.contenido.value
				})
			])
				.subscribe(([res, pub]) => {
					this.router.navigate(['/hilos', res.id, 1]);
				});
		}
	}

}
