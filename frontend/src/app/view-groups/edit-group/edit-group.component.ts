import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../../modal/modal.component';
import { SetTeacherComponent } from '../set-teacher/set-teacher.component';

@Component({
  selector: 'app-edit-group',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ModalComponent,
    SetTeacherComponent,
  ],
  templateUrl: './edit-group.component.html',
  styleUrl: './edit-group.component.css',
})
export class EditGroupComponent {
  feedbackMsg!: string;
  window = window;

  @Input() group: any;
  @Input() teachers: any[] = [];
  @Input() teacherAssignments: any[] = [];

  editGroup!: FormGroup;

  constructor(private dataService: DataService) {}

  receiveMessage($event: any) {
    this.teacherAssignments = this.teacherAssignments.filter(
      (e) => e.id !== $event.id
    );
    this.teacherAssignments.push($event);

    this.closeModal(
      'group-' + this.group.groupNumber + '-set-teacher-' + $event.name
    );
  }

  removeAssignment(teacherAssignment: any) {
    this.teacherAssignments = this.teacherAssignments.filter(
      (e) => e !== teacherAssignment
    );
  }

  getTeacherAssignment(teacher: any) {
    const teacherAssignment = this.teacherAssignments.find((e) => e.id == teacher.id);
    if (teacherAssignment == null) {
      return {id: teacher.id, name: teacher.name, daysPhase1: 0, daysPhase2: 0, daysPhase3: 0}
    } else {
      return teacherAssignment;
    }
  }

  ngOnInit() {
    this.initializeForm();
  }

  ngOnChanges(changes: any) {
    if (changes.group && !changes.group.firstChange) {
      this.initializeForm();
    }
  }

  private initializeForm() {
    this.editGroup = new FormGroup({
      field: new FormControl(this.group.field),
      color: new FormControl(this.group.color),
      numberOfStudents: new FormControl(this.group.numberOfStudents),
      startDate: new FormControl(this.group.startDate),
      daysPhase1: new FormControl(this.group.daysPhase1),
      weeksPhase1: new FormControl(this.group.weeksPhase1),
      daysPhase2: new FormControl(this.group.daysPhase2),
      weeksPhase2: new FormControl(this.group.weeksPhase2),
      daysPhase3: new FormControl(this.group.daysPhase3),
      weeksPhase3: new FormControl(this.group.weeksPhase3),
    });
  }

  onSubmit() {
    const formValue = this.editGroup.value;
    const data = {
      groupNumber: this.group.groupNumber,
      color: formValue.color,
      numberOfStudents: formValue.numberOfStudents,
      field: formValue.field,
      startDate: formValue.startDate,

      daysPhase1: formValue.daysPhase1,
      weeksPhase1: formValue.weeksPhase1,

      daysPhase2: formValue.daysPhase2,
      weeksPhase2: formValue.weeksPhase2,

      daysPhase3: formValue.daysPhase3,
      weeksPhase3: formValue.weeksPhase3,

      teacherAssignments: this.teacherAssignments,
    };
    this.dataService.putGroup(this.group.groupNumber, data).subscribe(
      (response) => {
        console.log('Response:', response);
        this.feedbackMsg = `Group ${response.groupNumber} succesfully edited`;
        this.showModal('feedback-edit-group-' + this.group.groupNumber);
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = `Error: ${error.error}`;
        this.showModal('feedback-edit-group-' + this.group.groupNumber);
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
