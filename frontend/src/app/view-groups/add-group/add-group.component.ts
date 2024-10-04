import { Component, Input } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { DataService } from '../data.service';
import { CommonModule } from '@angular/common';
import { ModalComponent } from "../../modal/modal.component";

@Component({
  selector: 'app-add-group',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './add-group.component.html',
  styleUrl: './add-group.component.css',
})
export class AddGroupComponent {
  feedbackMsg!: string;
  window = window;
  
  @Input() fields: any[] = [];

  addGroup = new FormGroup({
    groupNumber: new FormControl(''),
    color: new FormControl(''),
    numberOfStudents: new FormControl(''),
    field: new FormControl(''),
    startDate: new FormControl(''),
    weeksPhase1: new FormControl(''),
    weeksPhase2: new FormControl(''),
    weeksPhase3: new FormControl(''),
  });

  constructor(private dataService: DataService) {}

  onSubmit() {
    const data = this.addGroup.value;
    console.log(data);
    this.dataService.postGroup(data).subscribe(
      (response) => {
        console.log('Response:', response);
        this.feedbackMsg = `Group ${response.groupNumber} ${response.field.name} succesfully added and scheduled starting ${response.startDate}`;
        this.showModal('feedback')
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = `Error: ${error.error}`;
        this.showModal('feedback')
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
