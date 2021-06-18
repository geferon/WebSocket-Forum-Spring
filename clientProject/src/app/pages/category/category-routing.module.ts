import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CategoryComponent } from './category.component';

const routes: Routes = [
	{
		path: 'new',
		loadChildren: () => import('./new-category/new-category.module').then(m => m.NewThreadModule)
	},
	{
		path: ':idCategoria/new-thread',
		loadChildren: () => import('../new-thread/new-thread.module').then(m => m.NewThreadModule)
	},
	{
		path: ':id',
		component: CategoryComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class CategoryRoutingModule { }
