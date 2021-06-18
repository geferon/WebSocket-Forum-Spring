import { Injectable } from '@angular/core';
import {
	HttpRequest,
	HttpHandler,
	HttpEvent,
	HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
	providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

	constructor(
		private auth: AuthService
	) {}

	intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
		if (this.auth.isLoggedIn()) {
			request = request.clone({
				setHeaders: {
					'Authorization': `Basic ${this.auth.token}`
				}
			});
		}

		return next.handle(request);
	}
}
