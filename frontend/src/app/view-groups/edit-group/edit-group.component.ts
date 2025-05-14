import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../../modal/modal.component';

@Component({
  selector: 'app-edit-group',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './edit-group.component.html',
  styleUrl: './edit-group.component.css',
})
export class EditGroupComponent {
  feedbackMsg!: string;
  window = window;

  @Input() group: any;
  @Input() fields: any[] = [];

  editGroup!: FormGroup;

  constructor(private dataService: DataService) {}

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
      color: new FormControl(this.group.color),
      numberOfStudents: new FormControl(this.group.numberOfStudents),
      field: new FormControl(this.group.field),
      startDate: new FormControl(this.group.startDate),
      weeksPhase1: new FormControl(this.group.weeksPhase1),
      weeksPhase2: new FormControl(this.group.weeksPhase2),
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
      weeksPhase1: formValue.weeksPhase1,
      weeksPhase2: formValue.weeksPhase2,
      weeksPhase3: formValue.weeksPhase3,
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
