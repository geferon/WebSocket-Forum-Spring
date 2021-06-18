import { Location } from '@angular/common';
import { Injectable } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';

@Injectable({
	providedIn: 'root'
})
export class LocationHistoryService {

	constructor(
		private router: Router,
		private location: Location
	) { }

	private locationHistory: string[] = [];

	public init(): void {
		this.router.events.subscribe(event => {
			if (event instanceof NavigationStart) {
				if (event.navigationTrigger === 'popstate') {
					this.locationHistory.pop();
				} else {
					this.locationHistory.push(event.url);
				}
			}
		});
	}

	public canGoBack(): boolean {
		return this.locationHistory.length > 1;
	}

	public goBack(defaultRoute?: any): void {
		if (this.canGoBack()) {
			const normalizedRoute: string = (defaultRoute && Array.isArray(defaultRoute)) ?
				defaultRoute.join('/') :
				defaultRoute;
			const lastRoute = this.locationHistory[this.locationHistory.length - 2];
			const normalizedLastRoute = lastRoute.indexOf('#') != -1 ? lastRoute.substring(0, lastRoute.indexOf('#')) : lastRoute;
			if (!defaultRoute || normalizedLastRoute == normalizedRoute) {
				this.location.back();

				return;
			}
		}

		if (defaultRoute) {
			if (!Array.isArray(defaultRoute)) {
				defaultRoute = [defaultRoute];
			}
			this.router.navigate(defaultRoute);
			return;
		}

		throw new Error("Can't go back and there is no default route...");
	}
}
