import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
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
  setAvailability = new FormGroup({
    monday: new FormControl(''),
    tuesday: new FormControl(''),
    wednesday: new FormControl(''),
    thursday: new FormControl(''),
    friday: new FormControl(''),
  });

  constructor(private dataService: DataService) {}

  onSubmit() {
    const data = this.setAvailability.value;
    console.log(data);
    this.dataService.putAvailability(data).subscribe(
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
