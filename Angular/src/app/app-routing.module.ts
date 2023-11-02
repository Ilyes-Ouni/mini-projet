import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthenticationModule } from './authentication/authentication.module';
import { ClientsModule } from './clients/clients.module';
import { RegionsModule } from './regions/regions.module';

const routes: Routes = [
  {path: '', redirectTo: '/auth', pathMatch: 'full'},
  {path: 'auth', loadChildren: () => import('./authentication/authentication.module').then(m => m.AuthenticationModule)},
  {path: 'clients', loadChildren: () => import('./clients/clients.module').then(m => m.ClientsModule)},
  {path: 'clients-by-region', loadChildren: () => import('./clients-by-regions/clients-by-regions.module').then(m => m.ClientsByRegionsModule)},
  {path: 'regions', loadChildren: () => import('./regions/regions.module').then(m => m.RegionsModule)},
  {path: 'users', loadChildren: () => import('./users/users.module').then(m => m.UsersModule)},
  {path: '**', redirectTo: '/auth', pathMatch: 'full'},
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
