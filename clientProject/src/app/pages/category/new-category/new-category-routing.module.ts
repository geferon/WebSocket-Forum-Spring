import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewCategoryComponent } from './new-category.component';

const routes: Routes = [
	{
		path: '',
		component: NewCategoryComponent,
		pathMatch: 'full'
	},
	{
		path: ':id',
		component: NewCategoryComponent
	}
];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class NewCategoryRoutingModule { }
