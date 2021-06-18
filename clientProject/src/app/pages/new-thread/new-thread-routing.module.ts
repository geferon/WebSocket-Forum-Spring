import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NewThreadComponent } from './new-thread.component';

const routes: Routes = [{ path: '', component: NewThreadComponent }];

@NgModule({
	imports: [RouterModule.forChild(routes)],
	exports: [RouterModule]
})
export class NewThreadRoutingModule { }
