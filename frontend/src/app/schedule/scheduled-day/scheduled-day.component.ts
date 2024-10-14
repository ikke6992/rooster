import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalComponent } from "../../modal/modal.component";

@Component({
  selector: 'app-scheduled-day',
  standalone: true,
  imports: [CommonModule, ModalComponent],
  templateUrl: './scheduled-day.component.html',
  styleUrl: './scheduled-day.component.css',
})
export class ScheduledDayComponent {
  @Input() item: Scheduledday = {
    id: 0,
    date: new Date(),
    classroomId: 0,
    groupNumber: 0,
    groupColor: '#000000',
    field: '',
    teacher: '',
    note: '',
  };

  @Input() day: number = 0;

  @Input() classroom: number = 0;

  showNote() {
    document.getElementById(`myPopup${this.item.id}`)?.classList.toggle('show');
  }

  showModal() {
    let modal_t = document.getElementById('add-note');
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal() {
    let modal_t = document.getElementById('add-note');
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
}

export interface Scheduledday {
  id: number;
  date: Date;
  classroomId: number;
  groupNumber: number;
  groupColor: string;
  field: string;
  teacher: string;
  note: string;
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
