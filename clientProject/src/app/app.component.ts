import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';

import { AuthService } from './shared/services/auth.service';
import { LocationHistoryService } from './shared/services/location-history.service';
import { NavigationFocusService } from './shared/services/navigation-focus.service';
import { map, pairwise, startWith } from 'rxjs/operators';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
	private subscriptions = new Subscription();

	constructor(
		private location: LocationHistoryService,
		private navigationFocusService: NavigationFocusService
	) {
		this.subscriptions.add(navigationFocusService.navigationEndEvents.pipe(
			map(e => e.urlAfterRedirects),
			startWith(''),
			pairwise()
		).subscribe(([fromUrl, toUrl]) => {
			// We want to reset the scroll position on navigation except when navigating within
			// the documentation for a single component.
			if (!navigationFocusService.isNavigationWithinView(fromUrl, toUrl)) {
				resetScrollPosition();
			}
		}));
	}

	ngOnInit(): void {
		this.location.init();
	}

	ngOnDestroy(): void {
		this.subscriptions.unsubscribe();
	}
}

function resetScrollPosition() {
	if (typeof document === 'object' && document) {
		const content = document.querySelector('app-root > router-outlet + *');
		if (content) {
			content.scrollTop = 0;
		}
	}
}
