import { Routes } from '@angular/router';
import { ScheduleComponent } from './schedule/schedule.component';
import { ViewGroupsComponent } from './view-groups/view-groups.component';
import { ViewTeachersComponent } from './view-teachers/view-teachers.component';
import { FreeDaysComponent } from './free-days/free-days.component';
import { ViewFieldsComponent } from './view-fields/view-fields.component';
import { loggedInGuard } from './logged-in.guard';
import { ArchiveComponent } from './archive/archive.component';
import { environment } from '../environments/environment';

export const routes: Routes = [
  { path: `${environment.rootUrl}`, component: ScheduleComponent },
  {
    path: `${environment.rootUrl}/groups`,
    component: ViewGroupsComponent,
    canActivate: [loggedInGuard],
  },
  {
    path: `${environment.rootUrl}/fields`,
    component: ViewFieldsComponent,
    canActivate: [loggedInGuard],
  },
  {
    path: `${environment.rootUrl}/teachers`,
    component: ViewTeachersComponent,
    canActivate: [loggedInGuard],
  },
  {
    path: `${environment.rootUrl}/archive`,
    component: ArchiveComponent,
    canActivate: [loggedInGuard],
  },
  { path: `${environment.rootUrl}/free-days`, component: FreeDaysComponent },
  { path: `${environment.rootUrl}/**`, redirectTo: `${environment.rootUrl}` },
];
