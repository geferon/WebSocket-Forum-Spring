import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, NgModule, OnInit } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDialog } from '@angular/material/dialog';
import { filter, map } from 'rxjs/operators';

import { DialogsModule } from 'src/app/dialogs/dialogs.module';
import { AuthService } from 'src/app/shared/services/auth.service';
import { LoginDialogComponent } from 'src/app/dialogs/login-dialog/login-dialog.component';
import { DataService } from 'src/app/shared/services/data.service';

@Component({
	selector: 'app-navbar',
	templateUrl: './navbar.component.html',
	styleUrls: ['./navbar.component.scss']
})
export class NavBarComponent implements OnInit {

	constructor(
		public auth: AuthService,
		public data: DataService,
		private dialog: MatDialog
	) { }

	public subscripciones$ = this.data.getSubscriptionsLive();
	public subscripcionesNuevas$ = this.subscripciones$.pipe(
		map(subs => subs.filter(s => s.publicacionesPendientes > 0))
	);

	get user() {
		return this.auth.user;
	}

	getRoles() {
		return this.user.roles.map(r => 'user-' + r);
	}

	ngOnInit(): void {
	}

	loginOrRegister(): void {
		const loginDialog = this.dialog.open(LoginDialogComponent, {
			width: '300px'
		});
	}

	logout(): void {
		this.auth.logout();
	}

}


@NgModule({
	imports: [
		CommonModule,
		HttpClientModule,
		MatButtonModule,
		MatMenuModule,
		RouterModule,
		FlexLayoutModule,
		MatIconModule,
		MatBadgeModule,
		DialogsModule
	],
	exports: [NavBarComponent],
	declarations: [NavBarComponent]
})
export class NavBarModule {}
