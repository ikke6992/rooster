import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { DataService } from './data.service';
import { ScheduledDayComponent } from '../scheduled-day/scheduled-day.component';
import Holidays, { HolidaysTypes } from 'date-holidays';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [CommonModule, ScheduledDayComponent],
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css',
})
export class ScheduleComponent {
  // hd: Holidays = new Holidays('nl');
  data: Scheduledday[] = [];
  freeDays: FreeDay[] = [];

  month = new Date().getMonth() + 1;
  year = new Date().getFullYear();

  days: Day[] = this.daysInMonth(this.year, this.month);

  TOTAL_CLASSROOMS: number[] = Array(6)
    .fill(0)
    .map((_, index) => index + 1);

  daysInMonth(year: number, month: number): Day[] {    
    const array = Array(new Date(year, month, 0).getDate())
      .fill(0)
      .map((_, index) => index + 1);
    const days: Day[] = [];
    array.forEach(
      (value, index) =>
        (days[index] = {
          id: value,
          isWeekend: this.checkIfWeekend(year, month, value),
          isFreeDay: this.checkIfFreeDay(year, month, value),
        })
    );
    return days;
  }

  checkIfWeekend(year: number, month: number, day: number): boolean {
    const dayOfWeek = new Date(year, month - 1, day);
    return dayOfWeek.getDay() === 0 || dayOfWeek.getDay() === 6;
  }

  checkIfFreeDay(year: number, month: number, day: number) {
    const fday = new Date(year, month -1, day);    
    return !!this.freeDays.find((freeDay) => freeDay.date.getDate() == fday.getDate());
  }

  getMonthName(monthNumber: number): string {
    const date = new Date(this.year, monthNumber - 1);
    return new Intl.DateTimeFormat('en-US', { month: 'long' }).format(date);
  }

  public incrementMonth() {
    if (this.month === 12) {
      this.year++;
      this.month = 1;
      this.ngOnInit();
      return;
    }
    this.month++;
    this.ngOnInit();
  }

  public decrementMonth() {
    if (this.month === 1) {
      this.year--;
      this.month = 12;
      this.ngOnInit();
      return;
    }
    this.month--;
    this.ngOnInit();
  }

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService
    .getScheduledDaysByMonth(this.month, this.year)
    .subscribe((response: any[]) => {
      this.data = response;
      this.data.map((item) => (item.date = new Date(item.date)));
    });
    
    this.dataService
    .getFreeDaysByMonth(this.month, this.year)
    .subscribe((response: any[]) => {
      this.freeDays = response;
      this.freeDays.map((item) => (item.date = new Date(item.date)));
      this.days = this.daysInMonth(this.year, this.month);
    });
  }
}

export interface Scheduledday {
  id: number;
  date: Date;
  classroomId: number;
  groupNumber: number;
  groupColour: string;
  name: string;
}

interface Day {
  id: number;
  isWeekend: boolean;
  isFreeDay: boolean;
}

interface FreeDay {
  id: number;
  date: Date;
  name: string;
}

