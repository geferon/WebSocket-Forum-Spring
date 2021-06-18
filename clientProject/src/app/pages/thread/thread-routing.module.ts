import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ThreadComponent } from './thread.component';

const routes: Routes = [
	{
		path: ':idHilo/editar',
		pathMatch: 'full',
		loadChildren: () => import('../new-thread/new-thread.module').then(m => m.NewThreadModule)
	},
	{
		path: ':id',
		pathMatch: 'full',
		redirectTo: ':id/1'
	},
	{
		path: ':id/:page',
		component: ThreadComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class ThreadRoutingModule { }
