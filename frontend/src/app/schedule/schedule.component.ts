import { CommonModule } from '@angular/common';
import { Component, SimpleChanges, Input, OnChanges } from '@angular/core';
import { DataService } from './data.service';
import { ScheduledDayComponent } from '../scheduled-day/scheduled-day.component';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [CommonModule, ScheduledDayComponent],
  templateUrl: './schedule.component.html',
  styleUrl: './schedule.component.css'
})
export class ScheduleComponent {
  data: Scheduledday[] = [];
  
  month = new Date().getMonth()+1;
  year = new Date().getFullYear();

  days: number[] = new Array(this.daysInMonth(this.month,this.year)).fill(0).map((_, index) => index+1);

  TOTAL_CLASSROOMS: number[] = Array(6).fill(0).map((_, index) => index+1)


  daysInMonth(month: number, year: number) {
    return new Date(year, month, 0).getDate();
  }

  getMonthName(monthNumber: number): string {
    const date = new Date();
    date.setMonth(monthNumber - 1);
    return new Intl.DateTimeFormat('en-US', { month: 'long' }).format(date);
  }

  public incrementMonth() {
    if (this.month === 12) {
      this.year++
      this.ngOnInit()
      return this.month = 1;
    }
    this.month++;
    this.ngOnInit()
    return this.month;
  }

  public decrementMonth() {
    if (this.month === 1) {
      this.year--
      this.ngOnInit()
      return this.month = 12;
    }
    this.month--;
    this.ngOnInit()
    return this.month;
  }

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getScheduledDaysByMonth(this.month, this.year).subscribe((response: any[]) => {
      this.data = response;
      this.data.map((item) => item.date = new Date(item.date))
    });
  }
}

export interface Scheduledday {
  id: number,
  date: Date,
  classroomId: number,
  groupNumber: number,
  groupColour: string,
  name: string,
}
