import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { InjectableRxStompConfig } from '@stomp/ng2-stompjs';
import { BehaviorSubject, NEVER, Observable, of, timer } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { Usuario } from '../modelos';

export const TOKEN_KEY = 'auth-token';
export const USER_KEY = 'auth-user';

export function getToken() {
	return localStorage.getItem(TOKEN_KEY);
}

interface LoginResult {
	token: string;
}

interface RegisterResponse {
	token: string;
	message: string;
}

@Injectable({
	providedIn: 'root'
})
export class AuthService {
	get user(): Usuario {
		return this.jwtHelper.decodeToken(this.token);
	}

	get username(): string {
		return this.user?.username;
	}

	public get roles(): string[] {
		return this.user?.roles;
	}

	set token(token: string) {
		if (!token) {
			localStorage.removeItem(TOKEN_KEY);
		} else {
			localStorage.setItem(TOKEN_KEY, token);
		}
		this._token$.next(token);
	}

	get token(): string {
		return getToken();
	}

	private _token$ = new BehaviorSubject<string>(this.token);
	public token$ = this._token$.asObservable();

	public user$ = this.token$.pipe(map(t => this.jwtHelper.decodeToken(t) as Usuario));

	constructor(
		private http: HttpClient,
		private jwtHelper: JwtHelperService
	) {
		// Borrar token si esta expirado
		if (this.token && this.jwtHelper.isTokenExpired(this.token)) {
			this.token = null;
		}

		// Init countdown
		this.startRefreshTokenTimer();
	}

	public isLoggedIn(): boolean {
		return this.token != null && !this.jwtHelper.isTokenExpired(this.token);
	}

	public isRole(role: string): boolean {
		return this.user?.roles?.some(r => r == role) ?? false;
	}

	public login(username: string, password: string): Observable<boolean> {
		return this.http.post<LoginResult>('/api/auth/login', {
				username,
				password
			})
			.pipe(
				map(result => {
					this.token = result.token;

					return true;
				}),
				catchError(err => {
					console.error(err);
					return of(false);
				})
			);
	}

	public register(user: Usuario): Observable<RegisterResponse> {
		return this.http.post<RegisterResponse>('/api/auth/register', user)
			.pipe(
				tap((result) => {
					if (result.token) {
						this.token = result.token;
					}
				})
			);
	}

	public changePassword(oldPassword: string, newPassword: string): Observable<Boolean> {
		return this.http.post('/api/auth/password', {
			oldPassword,
			newPassword
		})
			.pipe(
				map(() => true),
				catchError(err => of(false))
			);
	}

	public refreshToken(): Observable<string> {
		if (!this.isLoggedIn()) {
			throw new Error('Cannot refresh token if not logged in or expired');
		}

		return this.http.post<LoginResult>('/api/auth/refreshtoken', {})
			.pipe(
				map(r => r.token),
				tap(t => this.token = t)
			);
	}

	public logout() {
		this.token = null;
	}

	private startRefreshTokenTimer(): void {
		this.token$
			.pipe(
				switchMap(t => ((!t || this.jwtHelper.isTokenExpired(t)) ? NEVER :
					timer(
						(this.jwtHelper.decodeToken(t).exp * 1000) - Date.now() - (60 * 1000)
					)
				)),
				switchMap(() => this.refreshToken())
			)
			.subscribe(() => {
				console.log("Token refreshed!");
			});
	}
}
