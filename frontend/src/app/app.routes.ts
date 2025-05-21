import { Routes } from '@angular/router';
import { ScheduleComponent } from './schedule/schedule.component';
import { ViewGroupsComponent } from './view-groups/view-groups.component';
import { ViewTeachersComponent } from './view-teachers/view-teachers.component';
import { FreeDaysComponent } from './free-days/free-days.component';
import { loggedInGuard } from './logged-in.guard';
import { ArchiveComponent } from './archive/archive.component';

export const routes: Routes = [
  { path: '', component: ScheduleComponent },
  { path: 'groups', component: ViewGroupsComponent, canActivate: [loggedInGuard] },
  { path: 'teachers', component: ViewTeachersComponent, canActivate: [loggedInGuard] },
  { path: 'archive', component: ArchiveComponent, canActivate: [loggedInGuard]},
  { path: 'free-days', component: FreeDaysComponent },
  { path: '**', redirectTo: ''}
];
