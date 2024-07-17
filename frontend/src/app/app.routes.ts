import { Routes } from '@angular/router';
import { ScheduleComponent } from './schedule/schedule.component';
import { ViewGroupsComponent } from './view-groups/view-groups.component';

export const routes: Routes = [
    { path: 'schedule', component: ScheduleComponent },
    { path: 'group', component: ViewGroupsComponent },
];
