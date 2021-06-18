import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from "./shared/services/auth.guard";

const routes: Routes = [
	{ path: 'logout', loadChildren: () => import('./pages/logout/logout.module').then(m => m.LogoutModule) },
	{ path: 'categorias', loadChildren: () => import('./pages/category/category.module').then(m => m.CategoryModule) },
	{ path: 'hilos', loadChildren: () => import('./pages/thread/thread.module').then(m => m.ThreadModule) },
	{ path: 'usuarios', loadChildren: () => import('./pages/profile/profile.module').then(m => m.ProfileModule) },
	{
		path: 'ajustes',
		loadChildren: () => import('./pages/settings/settings.module').then(m => m.SettingsModule),
		canActivate: [AuthGuard]
	},
	{
		path: '',
		pathMatch: 'full',
		loadChildren: () => import('./pages/home/home.module').then(m => m.HomeModule)
	}
];

@NgModule({
	imports: [
		RouterModule.forRoot(routes, {
			scrollPositionRestoration: 'enabled',
			anchorScrolling: 'enabled',
			relativeLinkResolution: 'corrected'
		})
	],
	exports: [RouterModule]
})
export class AppRoutingModule { }
