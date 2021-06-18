import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

import { AuthService } from 'src/app/shared/services/auth.service';
import { DataService } from 'src/app/shared/services/data.service';
import { Usuario } from 'src/app/shared/modelos';
import { PasswordChangeDialogComponent } from './password-change/password-change.component';

@Component({
	selector: 'app-settings',
	templateUrl: './settings.component.html',
	styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {

	public get usuario() {
		return this.auth.user;
	}
	// public settingsForm = new FormGroup({
	// 	username: new FormControl(this.usuario.username, [Validators.required]),
	// 	email: new FormControl(this.usuario.email, [Validators.required])
	// });
	public usuarioForm = new FormGroup({
		avatar: new FormControl(this.usuario.avatar, [Validators.required]),
		username: new FormControl(this.usuario.username, [Validators.required]),
		email: new FormControl(this.usuario.email, [Validators.required])
	});


	constructor(
		// private route: ActivatedRoute,
		private data: DataService,
		private auth: AuthService,
		private dialog: MatDialog
	) {
		console.log(this.usuario);
		this.usuarioForm.controls.avatar.valueChanges
		.subscribe(file => {
			if (typeof file === 'string') {
				if (!file.startsWith('data:')) {
					file = '/files' + (file.startsWith('/') ? '' : '/') + file;
				}
				this.avatarUrl = file;
			} else {
				const reader = new FileReader();
				reader.onload = () => {
					const fileUrl = reader.result.toString();
					this.avatarUrl = fileUrl;
				};
				reader.readAsDataURL(file);
			}
		});
	}

	isString(value: any): boolean {
		return typeof value === 'string';
	}

	public avatarUrl = '/files/' + this.usuario.avatar;

	onAvatarChange(event: Event): void {
		const file = (event.target as HTMLInputElement).files[0];
		this.usuarioForm.patchValue({avatar: file});
		this.usuarioForm.controls.avatar.markAsDirty();
	}

	changePassword(): void {
		const passwordDialog = this.dialog.open(PasswordChangeDialogComponent, {
			width: '400px'
		});
	}

	canSave(): boolean {
		return this.usuarioForm.dirty && this.usuarioForm.valid &&
			Object.entries(this.usuarioForm.value)
				.some(([key, value]) => this.usuario[key] != value);
	}

	save(): void {
		this.data.updateUsuario(this.usuario.id, this.usuarioForm.value)
			.pipe(
				switchMap(() => this.auth.refreshToken()),
				catchError(() => of({}))
			)
			.subscribe(() => {
				this.usuarioForm.setValue(this.usuario);
				this.usuarioForm.markAsPristine();
			});
	}

}
