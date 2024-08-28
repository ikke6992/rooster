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
  hd: Holidays = new Holidays('nl');
  data: Scheduledday[] = [];

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

  checkIfFreeDay(year: number, month: number, day: number): boolean {
    this.hd
      .getHolidays(2025)
      .filter(
        (holiday) =>
          holiday.type == 'public' ||
          holiday.name == 'Bevrijdingsdag' ||
          holiday.name == 'Goede Vrijdag'
      );
    const holiday = this.hd.isHoliday(new Date(year, month - 1, day));
    return (
      holiday &&
      (holiday[0].type === 'public' ||
        holiday[0].name === 'Bevrijdingsdag' ||
        holiday[0].name === 'Goede Vrijdag')
    );
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
    this.days = this.daysInMonth(this.year, this.month);
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
  name: string;
}

interface Day {
  id: number;
  isWeekend: boolean;
  isFreeDay: boolean;
}
