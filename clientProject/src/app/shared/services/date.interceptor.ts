import { Injectable } from '@angular/core';
import {
	HttpRequest,
	HttpHandler,
	HttpEvent,
	HttpInterceptor,
	HttpErrorResponse,
	HttpResponse
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

export const iso8601 = /^\d{4}-\d\d-\d\dT\d\d:\d\d:\d\d(\.\d+)?(([+-]\d\d:\d\d)|Z)?$/;

export function convertToDate(body) {
	if (body === null || body === undefined) {
		return body;
	}

	if (typeof body !== 'object') {
		return body;
	}

	for (const key of Object.keys(body)) {
		const value = body[key];
		if (isIso8601(value)) {
			body[key] = new Date(value);
		} else if (typeof value === 'object') {
			convertToDate(value);
		}
	}

	return body;
}

export function isIso8601(value) {
	if (value === null || value === undefined) {
		return false;
	}

	return iso8601.test(value);
}


export class DateHttpInterceptor implements HttpInterceptor {
	// Migrated from AngularJS https://raw.githubusercontent.com/Ins87/angular-date-interceptor/master/src/angular-date-interceptor.js

	public intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return next.handle(req).pipe(
			tap((event: HttpEvent<any>) => {
				if (event instanceof HttpResponse) {
					const body = event.body;
					convertToDate(body);
				}
			}, (err: any) => {
				if (err instanceof HttpErrorResponse) {
					if (err.status === 401) {
					}
				}
			}),
		);
	}
}
