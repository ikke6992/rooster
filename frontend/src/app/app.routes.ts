import { Routes } from '@angular/router';
import { ScheduleComponent } from './schedule/schedule.component';
import { ViewGroupsComponent } from './view-groups/view-groups.component';
import { ViewTeachersComponent } from './view-teachers/view-teachers.component';
import { FreeDaysComponent } from './free-days/free-days.component';

export const routes: Routes = [
  { path: 'schedule', component: ScheduleComponent },
  { path: 'groups', component: ViewGroupsComponent },
  { path: 'teachers', component: ViewTeachersComponent },
  { path: 'free-days', component: FreeDaysComponent}
];
