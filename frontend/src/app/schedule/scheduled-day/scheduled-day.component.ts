import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../../modal/modal.component';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';

@Component({
  selector: 'app-scheduled-day',
  standalone: true,
  imports: [CommonModule, ModalComponent, ReactiveFormsModule],
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
    isExam: true,
  };

  @Input() day: number = 0;

  @Input() classroom: number = 0;


  addNote!: FormGroup;
  feedbackMsg!: string;
  window: any = window;
  timerId: any;
  isLoggedIn: boolean = localStorage.getItem('token') !== null

  BRIGHTNESS_THRESHOLD: number = 128;
  
  constructor(private dataService: DataService) {}

  showNote() {
    var popup = document.getElementById(`myPopup${this.item.id}`)
    if (popup?.classList.contains('show')) {
      popup?.classList.remove('show')
    } else {
      popup?.classList.add('show');
    }
  }

  startTimer(){
    this.timerId = setTimeout(() => {document.getElementById(`myPopup${this.item.id}`)?.classList.remove('show');    
    }, 2000);
  }

  pauseTimeout() {
    if (this.timerId) {
      clearTimeout(this.timerId);
    }
  }

  removeLesson(){
if (window.confirm(`Remove Lesson from Group ${this.item.groupNumber} ${this.item.field} on ${this.item.date.toDateString()}`)) {
      this.dataService.removeLesson(this.item.id).subscribe(
        (response) => {
          console.log('Response:', response);
          window.location.reload();
        },
        (error) => {
          console.error('Error:', error);
        }
      );
    }
  }

  showModal(name: string) {    
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
  onSubmit() {
    const data = this.addNote.value;
    console.log(data);
    this.dataService.editNote(this.item.id, data).subscribe(
      (response) => {
        console.log('Response:', response);
        this.feedbackMsg = this.addNote.value.note === '' ? `Note successfully removed` : `Note ${data.note} successfully added`;
        this.showModal(`feedback-${this.item.id}`);
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = 'Error: ' + error.error;
        this.showModal(`feedback-${this.item.id}`);
      }
    );
  }

  ngOnInit() {
    this.initializeForm();
  }


  invertHex(hex: string){
    return (Number(`0x1${hex.substring(1)}`) ^ 0xFFFFFF).toString(16).substr(1).toUpperCase()
  }


  private initializeForm() {
    this.addNote = new FormGroup({
      note: new FormControl(this.item.note),
      isExam: new FormControl(this.item.isExam)
    });
  }

  preventDrag(event: DragEvent){
    event.preventDefault();
  }

  calculateBrightness(color: string): boolean{
    const r = parseInt(color.substring(1, 3), 16);
    const g = parseInt(color.substring(3, 5), 16);
    const b = parseInt(color.substring(5, 7), 16);

    const brightness = Math.sqrt(0.299*r*r + 0.587*g*g + 0.114*b*b);    
    
    return brightness < this.BRIGHTNESS_THRESHOLD;
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
  isExam: boolean;
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
