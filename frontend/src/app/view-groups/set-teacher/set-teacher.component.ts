import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-set-teacher',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './set-teacher.component.html',
  styleUrl: './set-teacher.component.css',
})
export class SetTeacherComponent {
  @Input() teacher: any;
  @Output() messageEvent = new EventEmitter<any>();

  addTeacher!: FormGroup;

  ngOnInit() {
    this.initializeForm();
  }

  private initializeForm() {
    this.addTeacher = new FormGroup({
      daysPhase1: new FormControl(this.teacher.daysPhase1),
      daysPhase2: new FormControl(this.teacher.daysPhase2),
      daysPhase3: new FormControl(this.teacher.daysPhase3),
    });
  }

  sendMessage() {
    const formValue = this.addTeacher.value;
    const teacherAssignment = {
      name: this.teacher.name,
      id: this.teacher.id,
      daysPhase1: formValue.daysPhase1,
      daysPhase2: formValue.daysPhase2,
      daysPhase3: formValue.daysPhase3,
    };
    this.messageEvent.emit(teacherAssignment);
  }

  closeModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
}
