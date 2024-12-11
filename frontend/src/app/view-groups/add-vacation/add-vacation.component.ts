import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from '../data.service';

@Component({
  selector: 'app-add-vacation',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-vacation.component.html',
  styleUrl: './add-vacation.component.css',
})
export class AddVacationComponent {
  @Input() group: Group = {
    groupNumber: 0,
    field: '',
  };

  addVacation = new FormGroup({
    startDate: new FormControl(),
    weeks: new FormControl(0),
  });

  constructor(private dataService: DataService) {}

  onSubmit() {
    const data = this.addVacation.value;
    console.log(data);
    this.dataService.addVacation(this.group.groupNumber, data).subscribe(
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

interface Group {
  groupNumber: number;
  field: string;
}
