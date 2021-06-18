import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DataService } from 'src/app/shared/services/data.service';

let colorRegex = /^(?:#(?:[A-Fa-f0-9]{3}){1,2}|(?:rgb[(](?:\s*0*(?:\d\d?(?:\.\d+)?(?:\s*%)?|\.\d+\s*%|100(?:\.0*)?\s*%|(?:1\d\d|2[0-4]\d|25[0-5])(?:\.\d+)?)\s*(?:,(?![)])|(?=[)]))){3}|hsl[(]\s*0*(?:[12]?\d{1,2}|3(?:[0-5]\d|60))\s*(?:\s*,\s*0*(?:\d\d?(?:\.\d+)?\s*%|\.\d+\s*%|100(?:\.0*)?\s*%)){2}\s*|(?:rgba[(](?:\s*0*(?:\d\d?(?:\.\d+)?(?:\s*%)?|\.\d+\s*%|100(?:\.0*)?\s*%|(?:1\d\d|2[0-4]\d|25[0-5])(?:\.\d+)?)\s*,){3}|hsla[(]\s*0*(?:[12]?\d{1,2}|3(?:[0-5]\d|60))\s*(?:\s*,\s*0*(?:\d\d?(?:\.\d+)?\s*%|\.\d+\s*%|100(?:\.0*)?\s*%)){2}\s*,)\s*0*(?:\.\d+|1(?:\.0*)?)\s*)[)])$/i;

let colorValidator = (ctrl: FormControl) => {
	let input = ctrl.value;
	if (colorRegex.test(input)) return null;

	return {color: true};
};

@Component({
	selector: 'app-new-category',
	templateUrl: './new-category.component.html',
	styleUrls: ['./new-category.component.scss']
})
export class NewCategoryComponent implements OnInit {

	public categoriaID: number = null;

	public categoriaForm = new FormGroup({
		id: new FormControl(),
		icono: new FormControl('sentiment_very_satisfied', [Validators.required]),
		nombre: new FormControl('', [Validators.required]),
		descripcion: new FormControl('', [Validators.required]),
		color: new FormControl('', [Validators.required, colorValidator])
	});

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private data: DataService
	) { }

	ngOnInit(): void {
		const routeParams = this.route.snapshot.paramMap;
		this.categoriaID = parseInt(routeParams.get('id'));

		if (this.categoriaID) {
			this.categoriaForm.disable();
			this.data.getCategoria(this.categoriaID)
			.subscribe(s => {
				this.categoriaForm.enable();
				this.categoriaForm.setValue(s);
			});
		}
	}

	publicar(): void {
		(this.categoriaID ? this.data.updateCategoria(this.categoriaForm.value) : this.data.createCategoria(this.categoriaForm.value))
		.subscribe(res => {
			this.router.navigate(['/categorias', res.id]);
		});
	}

}
