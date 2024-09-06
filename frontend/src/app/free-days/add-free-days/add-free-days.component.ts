import { Component, Input } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data-service';
import { CommonModule } from '@angular/common';
import Holidays, { HolidaysTypes } from 'date-holidays';

@Component({
  selector: 'app-add-free-days',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-free-days.component.html',
  styleUrl: './add-free-days.component.css',
})
export class AddFreeDaysComponent {
  hd: Holidays = new Holidays('nl');
  addOne: boolean = true;
  holidays: HolidaysTypes.Holiday[] = [];
  @Input() fields: any[] = [];

  addFreeday = new FormGroup({
    name: new FormControl(''),
    date: new FormControl(''),
  });

  addMultipleFreedays: FormGroup

  constructor(private dataService: DataService, private fb: FormBuilder) {
    this.holidays = this.hd
    .getHolidays(2025)
    .filter(
      (holiday) =>
        holiday.type == 'public' ||
        holiday.name == 'Bevrijdingsdag' ||
        holiday.name == 'Goede Vrijdag'
    );
    this.addMultipleFreedays = this.fb.group({
      year: 2024,
      array: this.fb.array(this.holidays.map(holiday => new FormControl(false)))
    });
  }
  
  get checkboxesArray(): FormArray {
    return this.addMultipleFreedays.get('array') as FormArray;
  }

  onSubmit() {
    const data = this.addFreeday.value;
    console.log(data);
    this.dataService.addDay(data).subscribe(
      (response) => {
        console.log('Response:', response);
        window.location.reload();
      },
      (error) => {
        console.error('Error:', error);
      }
    );
  }

  onSubmit2() {
    const data = this.addMultipleFreedays .value;
    console.log(data);
    this.dataService.addMultipleDays(data).subscribe(
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
