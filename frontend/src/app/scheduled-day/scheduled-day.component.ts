import { Component } from '@angular/core';
import { DataService } from './data.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-scheduled-day',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './scheduled-day.component.html',
  styleUrl: './scheduled-day.component.css',
})
export class ScheduledDayComponent {
  data: Scheduledday[] = [];

  days: number[] = new Array(this.daysInMonth(7,2024)).fill(0).map((item, index) => index+1);

  TOTAL_CLASSROOMS: number[] = Array(6).fill(0).map((item, index) => index+1)

  daysInMonth(month: number, year: number) {
    return new Date(year, month, 0).getDate();
  }

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getData().subscribe((response: any[]) => {
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

// export interface Classroom {
//   id: number
//   capacity: number,
//   hasBeamer: boolean,
//   forPracticum: boolean
// }

// export interface Lesson {
//   id: number,
//   isPracticum: boolean
// }
