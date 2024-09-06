import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';

@Component({
  selector: 'app-add-teacher',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-teacher.component.html',
  styleUrl: './add-teacher.component.css',
})
export class AddTeacherComponent {

  constructor(private dataService: DataService) {}

  addTeacher = new FormGroup({
    name: new FormControl(''),
    teachesPracticum: new FormControl(false),
    monday: new FormControl(true),
    tuesday: new FormControl(true),
    wednesday: new FormControl(true),
    thursday: new FormControl(true),
    friday: new FormControl(true),
    maxDaysPerWeek: new FormControl(0),
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
      teachesPracticum: formValue.teachesPracticum,
      availability: availability,
      maxDaysPerWeek: formValue.maxDaysPerWeek,
    }
    console.log(data);
    this.dataService.postTeacher(data).subscribe(
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
