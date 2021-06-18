import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { Injectable, NgModule, LOCALE_ID } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { FlexLayoutModule } from '@angular/flex-layout';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { InjectableRxStompConfig, RxStompService, rxStompServiceFactory } from '@stomp/ng2-stompjs';
import { JwtModule } from '@auth0/angular-jwt';
import { TimeagoCustomFormatter, TimeagoFormatter, TimeagoIntl, TimeagoModule } from 'ngx-timeago';
import { strings as spanishStrings } from 'ngx-timeago/language-strings/es';
import { ColorPickerModule } from 'ngx-color-picker';

import { IconPickerModule } from './shared/components/ngx-icon-picker';
import { DateHttpInterceptor } from './shared/services/date.interceptor';
// import { AuthInterceptor } from './services/auth.interceptor';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavBarModule } from './shared/components/navbar/navbar.component';
import { myRxStompConfig } from './my-rx-stomp.config';
import { getToken } from './shared/services/auth.service';

import '@angular/common/locales/global/es';

@Injectable({
	providedIn: 'root'
})
export class MyIntl extends TimeagoIntl {
	constructor() {
		super();
		this.strings = spanishStrings;
		this.changes.next();
	}
}


@NgModule({
	imports: [
		BrowserModule,
		HttpClientModule,
		ReactiveFormsModule,
		AppRoutingModule,
		BrowserAnimationsModule,
		FormsModule,
		FlexLayoutModule,
		IconPickerModule,
		ColorPickerModule,
		JwtModule.forRoot({
			config: {
				tokenGetter: getToken,
				allowedDomains: ["localhost:8080", "foro.geferon.net"]
			}
		}),
		TimeagoModule.forRoot({
			intl: { provide: TimeagoIntl, useClass: MyIntl },
			formatter: { provide: TimeagoFormatter, useClass: TimeagoCustomFormatter }
		}),

		NavBarModule
	],
	providers: [
		// No need to use it, provided by JWTModule
		{
			provide: HTTP_INTERCEPTORS,
			useClass: DateHttpInterceptor,
			multi: true
		},
		{
			provide: InjectableRxStompConfig,
			useValue: myRxStompConfig
		},
		{
			provide: RxStompService,
			useFactory: rxStompServiceFactory,
			deps: [InjectableRxStompConfig]
		},
		{
			provide: LOCALE_ID,
			useValue: 'es-ES'
		}
	],
	declarations: [
		AppComponent
	],
	bootstrap: [AppComponent]
})
export class AppModule { }
