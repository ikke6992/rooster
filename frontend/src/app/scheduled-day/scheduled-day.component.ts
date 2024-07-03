import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-scheduled-day',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './scheduled-day.component.html',
  styleUrl: './scheduled-day.component.css',
})
export class ScheduledDayComponent {
  @Input() item: Scheduledday = {
  id: 0,
  date: new Date(),
  classroom_id: 0,
 }

 @Input() day: number = 0

 @Input() classroom: number = 0
 

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
