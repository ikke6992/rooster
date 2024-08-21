import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';

@Component({
  selector: 'app-set-availability',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './set-availability.component.html',
  styleUrl: './set-availability.component.css',
})
export class SetAvailabilityComponent {
  @Input() teacher: Teacher = {
    id: 0,
    name: '',
    availability: [],
    maxDaysPerWeek: 0,
    groups: [],
  };

  setAvailability!: FormGroup;

  constructor(private dataService: DataService) {}

  ngOnInit() {
    this.initializeForm();
  }

  ngOnChanges(changes: any) {
    if (changes.teacher && !changes.teacher.firstChange) {
      this.initializeForm();
    }
  }

  private initializeForm() {
    this.setAvailability = new FormGroup({
      monday: new FormControl(this.teacher.availability.includes('MONDAY')),
      tuesday: new FormControl(this.teacher.availability.includes('TUESDAY')),
      wednesday: new FormControl(
        this.teacher.availability.includes('WEDNESDAY')
      ),
      thursday: new FormControl(this.teacher.availability.includes('THURSDAY')),
      friday: new FormControl(this.teacher.availability.includes('FRIDAY')),
      maxDaysPerWeek: new FormControl(this.teacher.maxDaysPerWeek),
    });
  }

  onSubmit() {
    const formValue = this.setAvailability.value;
    console.log(formValue);

    // Create an array of selected days
    const availability = [];
    if (formValue.monday) availability.push('MONDAY');
    if (formValue.tuesday) availability.push('TUESDAY');
    if (formValue.wednesday) availability.push('WEDNESDAY');
    if (formValue.thursday) availability.push('THURSDAY');
    if (formValue.friday) availability.push('FRIDAY');

    const data = {
      availability: availability,
      maxDaysPerWeek: formValue.maxDaysPerWeek,
    };

    console.log(data);
    this.dataService.putAvailability(this.teacher.id, data).subscribe(
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

interface Teacher {
  id: number,
  name: string,
  availability: any[],
  maxDaysPerWeek: number,
  groups: any[],
}
