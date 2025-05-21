import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ModalComponent } from '../../modal/modal.component';
import { CommonModule } from '@angular/common';
import { DataService } from '../data.service';

@Component({
  selector: 'app-add-lesson',
  standalone: true,
  imports: [    CommonModule,
      ReactiveFormsModule,
      ModalComponent,],
  templateUrl: './add-lesson.component.html',
  styleUrl: './add-lesson.component.css'
})
export class AddLessonComponent {
  addLesson = new FormGroup({
    group: new FormControl(),
    teacher: new FormControl(),
    date: new FormControl(),
  });
  feedbackMsg!: string;
  window: Window = window;

  @Input() teachers: any[] = [];
  @Input() groups: any[] = [];

  errorMsg!: string;

  constructor(private dataService: DataService) {}
  
  onSubmit(){
    console.log('meow');
    const formValue = this.addLesson.value;
    const data = {
      groupId: formValue.group,
      teacherId: formValue.teacher,
      date: formValue.date
    };
    this.dataService.addLesson(data).subscribe(
      (response) => {
        console.log('Response:', response);
        this.feedbackMsg = `Succesfully added lesson for group ${response.groupNumber} ${response.field.name} on ${response.startDate}`;
        this.showModal('feedback-add-lesson');
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = `Error: ${error.error}`;
        this.showModal('feedback-add-lesson');
      }
    );
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
}
