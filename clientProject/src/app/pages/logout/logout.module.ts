import { NgModule, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LogoutRoutingModule } from './logout-routing.module';
import { LogoutComponent } from './logout.component';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/shared/services/auth.service';


@NgModule({
	declarations: [LogoutComponent],
	imports: [
		CommonModule,
		LogoutRoutingModule
	]
})
export class LogoutModule {}
