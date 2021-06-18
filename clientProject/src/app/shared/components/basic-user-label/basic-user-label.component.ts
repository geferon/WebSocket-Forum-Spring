import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectionStrategy, NgModule, Input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Usuario } from 'src/app/shared/modelos';

@Component({
	selector: 'app-basic-user-label',
	templateUrl: './basic-user-label.component.html',
	styleUrls: ['./basic-user-label.component.scss']
})
export class BasicUserLabelComponent {

	constructor() { }

	@Input() user: Usuario;

	getRoles() {
		return this.user.roles.map(r => 'user-' + r);
	}

}

@NgModule({
	imports: [
		CommonModule,
		RouterModule
	],
	exports: [BasicUserLabelComponent],
	declarations: [BasicUserLabelComponent]
})
export class BasicUserLabelModule { }
