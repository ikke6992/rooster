import { Routes } from '@angular/router';
import { ScheduleComponent } from './schedule/schedule.component';
import { ViewGroupsComponent } from './view-groups/view-groups.component';
import { ViewTeachersComponent } from './view-teachers/view-teachers.component';
import { FreeDaysComponent } from './free-days/free-days.component';
import { ViewFieldsComponent } from './view-fields/view-fields.component';
import { loggedInGuard } from './logged-in.guard';

export const routes: Routes = [
  { path: '', component: ScheduleComponent },
  { path: 'groups', component: ViewGroupsComponent, canActivate: [loggedInGuard] },
  { path: 'fields', component: ViewFieldsComponent, canActivate: [loggedInGuard] },
  { path: 'teachers', component: ViewTeachersComponent, canActivate: [loggedInGuard] },
  { path: 'free-days', component: FreeDaysComponent },
  { path: '**', redirectTo: ''}
];
