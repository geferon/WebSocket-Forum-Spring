import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { LoginDialogComponent } from './login-dialog/login-dialog.component';

@Injectable({
	providedIn: 'root'
})
export class DialogsService {

	constructor(
		private dialog: MatDialog
	) { }

	loginDialog() {
		const loginDialog = this.dialog.open(LoginDialogComponent, {
			width: '300px'
		});

		return loginDialog;
	}
}
