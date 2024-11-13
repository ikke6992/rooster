import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';
import { ModalComponent } from "../../modal/modal.component";

@Component({
  selector: 'app-add-teacher',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ModalComponent],
  templateUrl: './add-teacher.component.html',
  styleUrl: './add-teacher.component.css',
})
export class AddTeacherComponent {
feedbackMsg: any;
window = window;

  constructor(private dataService: DataService) {}

  addTeacher = new FormGroup({
    name: new FormControl(''),
    monday: new FormControl(true),
    tuesday: new FormControl(true),
    wednesday: new FormControl(true),
    thursday: new FormControl(true),
    friday: new FormControl(true),
    maxDaysPerWeek: new FormControl(''),
  });

  onSubmit() {
    const formValue = this.addTeacher.value;
    console.log(formValue);

    // Create an array of selected days
    const availability = [];
    if (formValue.monday) availability.push('MONDAY');
    if (formValue.tuesday) availability.push('TUESDAY');
    if (formValue.wednesday) availability.push('WEDNESDAY');
    if (formValue.thursday) availability.push('THURSDAY');
    if (formValue.friday) availability.push('FRIDAY');

    const data = {
      name: formValue.name,
      availability: availability,
      maxDaysPerWeek: formValue.maxDaysPerWeek,
    }
    console.log(data);
    this.dataService.postTeacher(data).subscribe(
      (response) => {
        console.log('Response:', response);
        this.feedbackMsg = `Teacher ${response.name} succesfully added`
        this.showModal('feedback-add-teacher');
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = `Error: ${error.error}`
        this.showModal('feedback-add-teacher');
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
