import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
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

  days: number[] = new Array(this.daysInMonth(7,2024)).fill(0).map((item, index) => index+1);

  TOTAL_CLASSROOMS: number[] = Array(6).fill(0).map((item, index) => index+1)

  month = 7;
  year = 2024;

  daysInMonth(month: number, year: number) {
    return new Date(year, month, 0).getDate();
  }

  constructor(private dataService: DataService) {}

  ngOnInit(): void {


    this.dataService.getScheduledDaysByMonth(this.month, this.year).subscribe((response: any[]) => {
      this.data = response;
      this.data.map((item) => item.date = new Date(item.date))
      console.table(this.data);
      
    });
  }
}

export interface Scheduledday {
  id: number,
  date: Date,
  classroom_id: number,
}
