import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DataService } from './data.service';
import { ScheduledDayComponent } from '../scheduled-day/scheduled-day.component';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [CommonModule, ScheduledDayComponent],
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css',
})
export class ScheduleComponent {
  data: Scheduledday[] = [];

  month = new Date().getMonth() + 1;
  year = new Date().getFullYear();

  days: number[] = this.daysInMonth(this.month, this.year);
  weekend = false;

  TOTAL_CLASSROOMS: number[] = Array(6)
    .fill(0)
    .map((_, index) => index + 1);

  daysInMonth(month: number, year: number): number[] {
    return new Array(new Date(year, month, 0).getDate())
      .fill(0)
      .map((_, index) => index + 1);
  }

  getMonthName(monthNumber: number): string {
    const date = new Date(this.year, monthNumber - 1);
    return new Intl.DateTimeFormat('en-US', { month: 'long' }).format(date);
  }

  checkIfWeekend(day: number): boolean{
    const dayOfWeek = new Date()
    dayOfWeek.setDate(day)
    return dayOfWeek.getDay() === 0 || dayOfWeek.getDay() === 6 ;
  }

  public incrementMonth() {
    if (this.month === 12) {
      this.year++;
      this.ngOnInit();
      return (this.month = 1);
    }
    this.month++;
    this.ngOnInit();
    return this.month;
  }

  public decrementMonth() {
    if (this.month === 1) {
      this.year--;
      this.ngOnInit();
      return (this.month = 12);
    }
    this.month--;
    this.ngOnInit();
    return this.month;
  }

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.days = this.daysInMonth(this.month, this.year);
    this.dataService
      .getScheduledDaysByMonth(this.month, this.year)
      .subscribe((response: any[]) => {
        this.data = response;
        this.data.map((item) => (item.date = new Date(item.date)));
      });
  }
}

export interface Scheduledday {
  id: number;
  date: Date;
  classroomId: number;
  groupNumber: number;
  groupColour: string;
}
