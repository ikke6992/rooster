import { Component, Input } from '@angular/core';
import { ReactiveFormsModule, FormGroup, FormControl } from '@angular/forms';
import { DataService } from '../data.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-group',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-group.component.html',
  styleUrl: './add-group.component.css',
})
export class AddGroupComponent {
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
        //window.location.reload();
      },
      (error) => {
        console.error('Error:', error);
      }
    );
  }
}
