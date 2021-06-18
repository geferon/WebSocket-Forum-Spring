import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
	selector: 'app-logout',
	template: ''
})
export class LogoutComponent implements OnInit {
	constructor(
		private router: Router,
		private auth: AuthService
	) { }

	ngOnInit() {
		this.auth.logout();
		this.router.navigate(['/']);
	}
}
