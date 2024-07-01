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

  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getData().subscribe((response: any[]) => {
      this.data = response;
    });
  }
}

export interface Scheduledday {
  id: number,
  date: Date,
  classroom: Classroom,
  lesson: Lesson
}

export interface Classroom {
  id: number
  capacity: number,
  hasBeamer: boolean,
  forPracticum: boolean
}

export interface Lesson {
  id: number,
  isPracticum: boolean
}
