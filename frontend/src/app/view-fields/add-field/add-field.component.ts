import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ModalComponent } from '../../modal/modal.component';
import { DataService } from '../data.service';

@Component({
  selector: 'app-add-field',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './add-field.component.html',
  styleUrl: './add-field.component.css',
})
export class AddFieldComponent {
  feedbackMsg!: string;
  window = window;

  addField = new FormGroup({
    name: new FormControl(''),
    daysPhase1: new FormControl(),
    daysPhase2: new FormControl(),
    daysPhase3: new FormControl(),
  });

  constructor(private dataService: DataService) {}

  onSubmit() {
    const data = this.addField.value;
    this.dataService.postField(data).subscribe(
      (response) => {
        console.log('Response:', response);
        this.feedbackMsg = `Field ${response.name} succesfully added`;
        this.showModal('feedback');
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = `Error: ${error.error}`;
        this.showModal('feedback');
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
