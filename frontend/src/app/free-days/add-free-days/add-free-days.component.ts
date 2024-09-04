import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-free-days',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './add-free-days.component.html',
  styleUrl: './add-free-days.component.css'
})
export class AddFreeDaysComponent {
  addOne: boolean = true;
  @Input() fields: any[] = [];

  addFreeday = new FormGroup({
    name: new FormControl(''),
    date: new FormControl(''),
  });

  constructor(private dataService: DataService) {}

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
}