import { Component, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { DataService } from '../data-service';

@Component({
  selector: 'app-add-free-days',
  standalone: true,
  imports: [],
  templateUrl: './add-free-days.component.html',
  styleUrl: './add-free-days.component.css'
})
export class AddFreeDaysComponent {
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
}
