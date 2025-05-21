import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { ModalComponent } from '../../modal/modal.component';

@Component({
  selector: 'app-set-group',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './set-group.component.html',
  styleUrl: './set-group.component.css',
})
export class SetGroupComponent {
  feedbackMsg!: string;
  window = window;

  @Input() groups: any[] = [];
  @Input() teacher: any;

  addGroup = new FormGroup({
    setGroup: new FormControl(),
    daysPhase1: new FormControl(),
    daysPhase2: new FormControl(),
    daysPhase3: new FormControl(),
  });
  errorMsg!: string;

  constructor(private dataService: DataService) {}

  onSubmit() {
    const formValue = this.addGroup.value;
    const data = {
      daysPhase1: formValue.daysPhase1,
      daysPhase2: formValue.daysPhase2,
      daysPhase3: formValue.daysPhase3,
    };
    this.dataService
      .putGroup(this.teacher.id, formValue.setGroup, data)
      .subscribe(
        (response) => {
          console.log('Response:', response);
          this.feedbackMsg = `${this.teacher.name} assigned to Group ${response.groupNumber}`;
          this.showModal('feedback-set-group-' + this.teacher.name);
        },
        (error) => {
          console.error('Error:', error);
          this.feedbackMsg = `Error: ${error.error}`;
          this.showModal('feedback-set-group-' + this.teacher.name);
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
